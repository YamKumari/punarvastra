package com.punarvastra.service;

import com.punarvastra.dao.OrderDao;
import com.punarvastra.dao.OrderDaoImpl;
import com.punarvastra.dao.ProductDao;
import com.punarvastra.dao.ProductDaoImpl;
import com.punarvastra.dao.UserDao;
import com.punarvastra.dao.UserDaoImpl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Admin dashboard and analytics aggregates.
 */
public class AnalyticsService {

    private final OrderDao orderDao = new OrderDaoImpl();
    private final ProductDao productDao = new ProductDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    public long countProducts() throws SQLException {
        return productDao.countAll();
    }

    public long countOrders() throws SQLException {
        return orderDao.countOrders();
    }

    public long countUsers() throws SQLException {
        return userDao.countUsers();
    }

    public BigDecimal revenueThisMonth() throws SQLException {
        return orderDao.sumRevenueCurrentMonth();
    }

    public List<Map<String, Object>> topProducts(int n) throws SQLException {
        return orderDao.topSellingProducts(n);
    }

    public List<Map<String, Object>> revenueByMonth(int months) throws SQLException {
        return orderDao.revenueByMonth(months);
    }

    public List<Map<String, Object>> ordersByStatus() throws SQLException {
        return orderDao.ordersCountByStatus();
    }

    public long lowStockCount() throws SQLException {
        return productDao.countLowStock(3);
    }

    public long pendingUsersCount() throws SQLException {
        return userDao.countPendingUsers();
    }

    public long pendingListingsCount() throws SQLException {
        return productDao.countByListingStatus("PENDING");
    }
}
