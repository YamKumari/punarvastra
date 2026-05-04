package com.punarvastra.dao;

import com.punarvastra.entity.CartItem;
import com.punarvastra.entity.Order;
import com.punarvastra.entity.OrderItem;
import com.punarvastra.entity.Product;
import com.punarvastra.utils.DatabaseConnection;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JDBC {@link OrderDao}.
 */
public class OrderDaoImpl implements OrderDao {


   @Override
    public int insertOrderHeader(int userId, BigDecimal total, String address, String phone, String payment)
            throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status, shipping_address, phone, payment_method) "
                + "VALUES (?,?, 'PENDING', ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setBigDecimal(2, total);
            ps.setString(3, address);
            ps.setString(4, phone);
            ps.setString(5, payment);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Order insert failed.");
    }

    @Override
    public void insertOrderItem(int orderId, int productId, int qty, BigDecimal priceEach) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            ps.setBigDecimal(4, priceEach);
            ps.executeUpdate();
        }
    }

    @Override
    public int placeOrderWithItems(int userId, List<CartItem> lines, String address, String phone, String payment)
            throws SQLException {
        if (lines == null || lines.isEmpty()) {
            throw new SQLException("Cart is empty.");
        }
        ProductDaoImpl pdao = new ProductDaoImpl();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem line : lines) {
            Product p = line.getProduct();
            if (p == null || p.getPrice() == null) {
                throw new SQLException("Invalid cart line.");
            }
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
        }
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            String sqlOrder = "INSERT INTO orders (user_id, total_amount, status, shipping_address, phone, payment_method) "
                    + "VALUES (?,?, 'PENDING', ?, ?, ?)";
            int orderId;
            try (PreparedStatement ps = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setBigDecimal(2, total);
                ps.setString(3, address);
                ps.setString(4, phone);
                ps.setString(5, payment);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No order id.");
                    }
                    orderId = keys.getInt(1);
                }
            }
            String sqlItem = "INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES (?,?,?,?)";
            for (CartItem line : lines) {
                Product p = line.getProduct();
                int n = pdao.decrementStock(conn, p.getId(), line.getQuantity());
                if (n != 1) {
                    throw new SQLException("Insufficient stock for: " + p.getTitle());
                }
                try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, p.getId());
                    ps.setInt(3, line.getQuantity());
                    ps.setBigDecimal(4, p.getPrice());
                    ps.executeUpdate();
                }
            }
            conn.commit();
            return orderId;
        } catch (Exception ex) {
            conn.rollback();
            if (ex instanceof SQLException) {
                throw (SQLException) ex;
            }
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    private static Order mapHeader(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setShippingAddress(rs.getString("shipping_address"));
        o.setPhone(rs.getString("phone"));
        o.setPaymentMethod(rs.getString("payment_method"));
        if (rs.getTimestamp("created_at") != null) {
            o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return o;
    }

    @Override
    public List<Order> findByUserId(int userId, String statusFilter) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE user_id=? ");
        if (statusFilter != null && !statusFilter.isBlank() && !"ALL".equalsIgnoreCase(statusFilter)) {
            sql.append("AND status=? ");
        }
        sql.append("ORDER BY created_at DESC");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setInt(1, userId);
            if (statusFilter != null && !statusFilter.isBlank() && !"ALL".equalsIgnoreCase(statusFilter)) {
                ps.setString(2, statusFilter.trim().toUpperCase());
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapHeader(rs));
                }
                return list;
            }
        }
    }

    @Override
    public Optional<Order> findByIdWithItems(int orderId, int userId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id=? AND user_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Order o = mapHeader(rs);
                o.setItems(loadItems(conn, orderId));
                return Optional.of(o);
            }
        }
    }

    @Override
    public Optional<Order> findByIdForAdmin(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Order o = mapHeader(rs);
                o.setItems(loadItems(conn, orderId));
                return Optional.of(o);
            }
        }
    }

    private List<OrderItem> loadItems(Connection conn, int orderId) throws SQLException {
        String sql = "SELECT oi.*, p.title AS product_title, p.image AS product_image FROM order_items oi "
                + "JOIN products p ON oi.product_id = p.id WHERE oi.order_id=?";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem it = new OrderItem();
                    it.setId(rs.getInt("id"));
                    it.setOrderId(rs.getInt("order_id"));
                    it.setProductId(rs.getInt("product_id"));
                    it.setQuantity(rs.getInt("quantity"));
                    it.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
                    it.setProductTitle(rs.getString("product_title"));
                    it.setProductImage(rs.getString("product_image"));
                    items.add(it);
                }
            }
        }
        return items;
    }

    @Override
    public void updateStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    @Override
    public long countOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0;
        }
    }

    @Override
    public BigDecimal sumRevenueCurrentMonth() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount),0) FROM orders WHERE status IN ('CONFIRMED','SHIPPED','DELIVERED') "
                + "AND YEAR(created_at)=YEAR(CURRENT_DATE()) AND MONTH(created_at)=MONTH(CURRENT_DATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }

    @Override
    public List<Map<String, Object>> topSellingProducts(int limit) throws SQLException {
        String sql = "SELECT p.id, p.title, SUM(oi.quantity) AS units "
                + "FROM order_items oi JOIN products p ON oi.product_id=p.id "
                + "JOIN orders o ON oi.order_id=o.id WHERE o.status IN ('CONFIRMED','SHIPPED','DELIVERED','PENDING') "
                + "GROUP BY p.id, p.title ORDER BY units DESC LIMIT ?";
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("title", rs.getString("title"));
                    m.put("units", rs.getLong("units"));
                    out.add(m);
                }
            }
        }
        return out;
    }

    @Override
    public List<Map<String, Object>> revenueByMonth(int months) throws SQLException {
        String sql = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS ym, COALESCE(SUM(total_amount),0) AS rev "
                + "FROM orders WHERE status IN ('CONFIRMED','SHIPPED','DELIVERED') "
                + "AND created_at >= DATE_SUB(CURRENT_DATE(), INTERVAL ? MONTH) "
                + "GROUP BY ym ORDER BY ym";
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, months);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("month", rs.getString("ym"));
                    m.put("revenue", rs.getBigDecimal("rev"));
                    out.add(m);
                }
            }
        }
        return out;
    }

    @Override
    public List<Map<String, Object>> ordersCountByStatus() throws SQLException {
        String sql = "SELECT status, COUNT(*) AS cnt FROM orders GROUP BY status";
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("status", rs.getString("status"));
                m.put("count", rs.getLong("cnt"));
                out.add(m);
            }
        }
        return out;
    }

    @Override
    public List<Order> findAllOrders(String statusFilter, int limit) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM orders ");
        if (statusFilter != null && !statusFilter.isBlank() && !"ALL".equalsIgnoreCase(statusFilter)) {
            sql.append("WHERE status=? ");
        }
        sql.append("ORDER BY created_at DESC LIMIT ?");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            if (statusFilter != null && !statusFilter.isBlank() && !"ALL".equalsIgnoreCase(statusFilter)) {
                ps.setString(i++, statusFilter.trim().toUpperCase());
            }
            ps.setInt(i, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapHeader(rs));
                }
                return list;
            }
        }
    }

    @Override
    public List<Order> findRecent(int limit) throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapHeader(rs));
                }
                return list;
            }
        }
    }

}
