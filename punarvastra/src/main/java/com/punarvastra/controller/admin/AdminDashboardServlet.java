package com.punarvastra.controller.admin;

import com.punarvastra.controller.ViewForwarder;
import com.punarvastra.service.AnalyticsService;
import com.punarvastra.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Admin home: KPI cards, recent orders, pending queues.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin", "/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AdminDashboardServlet.class.getName());
    private final AnalyticsService analyticsService = new AnalyticsService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Admin");
            request.setAttribute("totalProducts", analyticsService.countProducts());
            request.setAttribute("totalOrders", analyticsService.countOrders());
            request.setAttribute("totalUsers", analyticsService.countUsers());
            request.setAttribute("revenueMonth", analyticsService.revenueThisMonth());
            request.setAttribute("pendingUsers", analyticsService.pendingUsersCount());
            request.setAttribute("pendingListings", analyticsService.pendingListingsCount());
            request.setAttribute("lowStock", analyticsService.lowStockCount());
            request.setAttribute("recentOrders", orderService.recentOrders(8));
            ViewForwarder.forward(request, response, "admin/dashboard.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Admin dashboard", ex);
            throw new ServletException(ex);
        }
    }
}
