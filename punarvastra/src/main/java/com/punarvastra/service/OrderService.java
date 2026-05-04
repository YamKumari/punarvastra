package com.punarvastra.service;

import com.punarvastra.dao.OrderDao;
import com.punarvastra.dao.OrderDaoImpl;
import com.punarvastra.entity.CartItem;
import com.punarvastra.entity.Order;
import com.punarvastra.exception.ValidationException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


/**
 * Checkout and order history.
 */
public class OrderService {



    private final OrderDao orderDao = new OrderDaoImpl();
    private final CartService cartService = new CartService();

    /**
     * Places order from hydrated cart; clears cart on success.
     */
    public int placeOrder(jakarta.servlet.http.HttpServletRequest request, int userId,
                          String address, String phone, String paymentMethod)
            throws ValidationException, SQLException {
        if (address == null || address.isBlank()) {
            throw new ValidationException("Shipping address is required.");
        }
        String ph = phone != null ? phone.replaceAll("\\s+", "") : "";
        if (ph.length() != 10 || !ph.startsWith("9")) {
            throw new ValidationException("Valid 10-digit mobile starting with 9 is required.");
        }
        if (paymentMethod == null || (!paymentMethod.equals("COD") && !paymentMethod.equals("ESEWA"))) {
            paymentMethod = "COD";
        }
        cartService.hydrate(request);
        List<CartItem> lines = com.punarvastra.utils.SessionUtil.getCart(request);
        if (lines.isEmpty()) {
            throw new ValidationException("Your cart is empty.");
        }
        int orderId = orderDao.placeOrderWithItems(userId, lines, address.trim(), ph, paymentMethod);
        cartService.clear(request);
        return orderId;
    }

    public List<Order> listMyOrders(int userId, String status) throws SQLException {
        return orderDao.findByUserId(userId, status);
    }

    public Optional<Order> getOrderForUser(int orderId, int userId) throws SQLException {
        return orderDao.findByIdWithItems(orderId, userId);
    }

    public Optional<Order> getOrderAdmin(int orderId) throws SQLException {
        return orderDao.findByIdForAdmin(orderId);
    }

    public void updateOrderStatus(int orderId, String status) throws SQLException {
        orderDao.updateStatus(orderId, status);
    }

    public List<Order> recentOrders(int limit) throws SQLException {
        return orderDao.findRecent(limit);
    }

    public List<Order> listOrdersForAdmin(String statusFilter, int limit) throws SQLException {
        return orderDao.findAllOrders(statusFilter, limit);
    }

    public long countOrders() throws SQLException {
        return orderDao.countOrders();
    }
}
