package com.punarvastra.dao;

import com.punarvastra.entity.Product;
import com.punarvastra.utils.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC {@link ProductDao}.
 */
public class ProductDaoImpl implements ProductDao {

    private static Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        int sid = rs.getInt("seller_id");
        if (!rs.wasNull()) {
            p.setSellerId(sid);
        } else {
            p.setSellerId(null);
        }
        p.setTitle(rs.getString("title"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setSize(rs.getString("size"));
        p.setProductCondition(rs.getString("product_condition"));
        p.setBrand(rs.getString("brand"));
        p.setImage(rs.getString("image"));
        p.setStock(rs.getInt("stock"));
        int cid = rs.getInt("category_id");
        if (!rs.wasNull()) {
            p.setCategoryId(cid);
        }
        p.setListingStatus(rs.getString("listing_status"));
        if (rs.getTimestamp("created_at") != null) {
            p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        try {
            String cn = rs.getString("category_name");
            p.setCategoryName(cn);
        } catch (SQLException ignore) {
            // optional column
        }
        return p;
    }

    @Override
    public List<Product> findPublicCatalog(Integer categoryId, String size, BigDecimal minPrice,
                                           BigDecimal maxPrice, String condition, String sort, String keyword)
            throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.name AS category_name FROM products p "
                        + "LEFT JOIN categories c ON p.category_id = c.id "
                        + "WHERE p.listing_status = 'APPROVED' AND p.stock > 0 ");
        List<Object> params = new ArrayList<>();
        if (categoryId != null) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }
        if (size != null && !size.isBlank()) {
            sql.append("AND p.size = ? ");
            params.add(size.trim());
        }
        if (minPrice != null) {
            sql.append("AND p.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append("AND p.price <= ? ");
            params.add(maxPrice);
        }
        if (condition != null && !condition.isBlank()) {
            sql.append("AND p.product_condition = ? ");
            params.add(condition.trim());
        }
        if (keyword != null && !keyword.isBlank()) {
            String like = "%" + keyword.trim().replace("%", "\\%").replace("_", "\\_") + "%";
            sql.append("AND (p.title LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ");
            params.add(like);
            params.add(like);
            params.add(like);
        }
        if ("price_low".equals(sort)) {
            sql.append("ORDER BY p.price ASC, p.id DESC");
        } else if ("price_high".equals(sort)) {
            sql.append("ORDER BY p.price DESC, p.id DESC");
        } else {
            sql.append("ORDER BY p.created_at DESC, p.id DESC");
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
                return out;
            }
        }
    }

    @Override
    public Optional<Product> findApprovedById(int id) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p "
                + "LEFT JOIN categories c ON p.category_id = c.id "
                + "WHERE p.id = ? AND p.listing_status = 'APPROVED' AND p.stock > 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> findById(int id) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p "
                + "LEFT JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> findOwnedBySeller(int productId, int sellerId) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p "
                + "LEFT JOIN categories c ON p.category_id = c.id WHERE p.id = ? AND p.seller_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findFeaturedApproved(int limit) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p "
                + "LEFT JOIN categories c ON p.category_id = c.id "
                + "WHERE p.listing_status = 'APPROVED' AND p.stock > 0 "
                + "ORDER BY p.created_at DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
                return out;
            }
        }
    }

    @Override
    public List<Product> findBySeller(int sellerId) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p "
                + "LEFT JOIN categories c ON p.category_id = c.id WHERE p.seller_id = ? "
                + "ORDER BY FIELD(p.listing_status,'PENDING','REJECTED','APPROVED'), p.updated_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
                return out;
            }
        }
    }

    @Override
    public List<Product> findAllOrderByUpdated() throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p "
                + "LEFT JOIN categories c ON p.category_id = c.id ORDER BY p.updated_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Product> out = new ArrayList<>();
            while (rs.next()) {
                out.add(mapRow(rs));
            }
            return out;
        }
    }

    @Override
    public List<Product> findByListingStatus(String listingStatus) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p "
                + "LEFT JOIN categories c ON p.category_id = c.id "
                + "WHERE p.listing_status = ? ORDER BY p.updated_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, listingStatus);
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
                return out;
            }
        }
    }

    @Override
    public int insert(Product p) throws SQLException {
        String sql = "INSERT INTO products (seller_id, title, description, price, size, product_condition, "
                + "brand, image, stock, category_id, listing_status) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (p.getSellerId() != null) {
                ps.setInt(1, p.getSellerId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, p.getTitle());
            ps.setString(3, p.getDescription());
            ps.setBigDecimal(4, p.getPrice());
            ps.setString(5, p.getSize());
            ps.setString(6, p.getProductCondition());
            ps.setString(7, p.getBrand());
            ps.setString(8, p.getImage());
            ps.setInt(9, p.getStock());
            if (p.getCategoryId() != null) {
                ps.setInt(10, p.getCategoryId());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }
            ps.setString(11, p.getListingStatus() != null ? p.getListingStatus() : "PENDING");
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Insert product failed.");
    }

    @Override
    public void update(Product p) throws SQLException {
        String sql = "UPDATE products SET seller_id=?, title=?, description=?, price=?, size=?, "
                + "product_condition=?, brand=?, image=?, stock=?, category_id=?, listing_status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (p.getSellerId() != null) {
                ps.setInt(1, p.getSellerId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, p.getTitle());
            ps.setString(3, p.getDescription());
            ps.setBigDecimal(4, p.getPrice());
            ps.setString(5, p.getSize());
            ps.setString(6, p.getProductCondition());
            ps.setString(7, p.getBrand());
            ps.setString(8, p.getImage());
            ps.setInt(9, p.getStock());
            if (p.getCategoryId() != null) {
                ps.setInt(10, p.getCategoryId());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }
            ps.setString(11, p.getListingStatus());
            ps.setInt(12, p.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateListingStatus(int productId, String status) throws SQLException {
        String sql = "UPDATE products SET listing_status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    @Override
    public void resubmitToPending(int productId, int sellerId) throws SQLException {
        String sql = "UPDATE products SET listing_status='PENDING' WHERE id=? AND seller_id=? AND listing_status='REJECTED'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, sellerId);
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public long countApproved() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE listing_status='APPROVED'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public long countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0;
        }
    }

    @Override
    public long countByListingStatus(String listingStatus) throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE listing_status=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, listingStatus);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        }
    }

    @Override
    public long countLowStock(int threshold) throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE listing_status='APPROVED' AND stock < ? AND stock >= 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return 0;
    }

    @Override
    public int decrementStock(Connection conn, int productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate();
        }
    }
}
