package com.punarvastra.controller.admin;

import com.punarvastra.controller.ViewForwarder;
import com.punarvastra.service.AnalyticsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Admin analytics: top sellers, revenue trend, status breakdown, low stock.
 */
@WebServlet(name = "AdminAnalyticsServlet", urlPatterns = {"/admin/analytics"})
public class AdminAnalyticsServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AdminAnalyticsServlet.class.getName());
    private final AnalyticsService analyticsService = new AnalyticsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("topProducts", analyticsService.topProducts(5));
            request.setAttribute("revenueMonths", analyticsService.revenueByMonth(6));
            request.setAttribute("ordersByStatus", analyticsService.ordersByStatus());
            request.setAttribute("lowStockCount", analyticsService.lowStockCount());
            ViewForwarder.forward(request, response, "admin/analytics.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Admin analytics", ex);
            throw new ServletException(ex);
        }
    }
}
