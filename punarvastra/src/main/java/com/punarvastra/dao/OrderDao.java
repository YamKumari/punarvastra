package com.punarvastra.dao;

import com.punarvastra.entity.CartItem;
import com.punarvastra.entity.Order;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Orders and analytics-oriented queries.
 */
public interface OrderDao {

    int insertOrderHeader(int userId, BigDecimal total, String address, String phone, String payment)
            throws SQLException;

    void insertOrderItem(int orderId, int productId, int qty, BigDecimal priceEach) throws SQLException;

    /**
     * Atomic checkout: insert order, items, decrement stock; rollback on failure.
     */
    int placeOrderWithItems(int userId, List<CartItem> lines, String address, String phone, String payment)
            throws SQLException;

    List<Order> findByUserId(int userId, String statusFilter) throws SQLException;

    Optional<Order> findByIdWithItems(int orderId, int userId) throws SQLException;

    Optional<Order> findByIdForAdmin(int orderId) throws SQLException;

    void updateStatus(int orderId, String newStatus) throws SQLException;

    long countOrders() throws SQLException;

    BigDecimal sumRevenueCurrentMonth() throws SQLException;

    List<Map<String, Object>> topSellingProducts(int limit) throws SQLException;

    List<Map<String, Object>> revenueByMonth(int months) throws SQLException;

    List<Map<String, Object>> ordersCountByStatus() throws SQLException;

    List<Order> findRecent(int limit) throws SQLException;

    /** Admin order table with optional status filter (null or ALL = no filter). */
    List<Order> findAllOrders(String statusFilter, int limit) throws SQLException;
}
