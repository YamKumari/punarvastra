package com.punarvastra.controller;

import com.punarvastra.entity.User;
import com.punarvastra.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(OrderServlet.class.getName());
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User u = SessionUtil.getCurrentUser(session);
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            var opt = orderService.getOrderForUser(id, u.getId());
            if (opt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("pageTitle", "Order #" + id);
            request.setAttribute("order", opt.get());
            ViewForwarder.forward(request, response, "order-detail.jsp");
        } catch (NumberFormatException ex) {
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Order detail", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User u = SessionUtil.getCurrentUser(session);
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }
        String action = request.getParameter("action");
        if (!"place".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }
        try {
            String address = request.getParameter("shippingAddress");
            String phone = request.getParameter("phone");
            String pay = request.getParameter("paymentMethod");
            int orderId = orderService.placeOrder(request, u.getId(), address, phone, pay);
            SessionUtil.setFlashSuccess(session, "Order placed successfully.");
            response.sendRedirect(request.getContextPath() + "/order-success?id=" + orderId);
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Place order", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Checkout failed.");
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}
