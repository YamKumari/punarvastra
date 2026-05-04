package com.punarvastra.controller;

import com.punarvastra.entity.User;
import com.punarvastra.service.OrderService;
import com.punarvastra.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Authenticated user's order history with optional status filter.
 */
@WebServlet(name = "OrderHistoryServlet", urlPatterns = {"/orders"})
public class OrderHistoryServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(OrderHistoryServlet.class.getName());
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User u = SessionUtil.getCurrentUser(request.getSession(false));
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            request.setAttribute("pageTitle", "Orders");
            String st = request.getParameter("status");
            request.setAttribute("orders", orderService.listMyOrders(u.getId(), st));
            request.setAttribute("filterStatus", st);
            ViewForwarder.forward(request, response, "order-history.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Order history", ex);
            throw new ServletException(ex);
        }
    }
}
