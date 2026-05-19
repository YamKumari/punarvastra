package com.punarvastra.controller.admin;

import com.punarvastra.controller.ViewForwarder;
import com.punarvastra.entity.WeeklyData;
import com.punarvastra.service.AnalyticsService;
import com.punarvastra.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Admin home: KPI cards, recent orders, pending queues, and charts.
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

            // ========== CHART DATA ==========
            // Weekly Orders Data
            List<WeeklyData> weeklyOrders = analyticsService.getWeeklyOrdersCount();
            List<String> orderDates = new ArrayList<>();
            List<Integer> orderCounts = new ArrayList<>();
            for (WeeklyData wd : weeklyOrders) {
                orderDates.add(wd.getDate());
                orderCounts.add(wd.getCount());
            }
            request.setAttribute("orderDates", orderDates);
            request.setAttribute("orderCounts", orderCounts);

            // Weekly Users Data
            List<WeeklyData> weeklyUsers = analyticsService.getWeeklyUserRegistrations();
            List<String> userDates = new ArrayList<>();
            List<Integer> userCounts = new ArrayList<>();
            for (WeeklyData wd : weeklyUsers) {
                userDates.add(wd.getDate());
                userCounts.add(wd.getCount());
            }
            request.setAttribute("userDates", userDates);
            request.setAttribute("userCounts", userCounts);

            // Weekly Products Data
            List<WeeklyData> weeklyProducts = analyticsService.getWeeklyProductListings();
            List<String> productDates = new ArrayList<>();
            List<Integer> productCounts = new ArrayList<>();
            for (WeeklyData wd : weeklyProducts) {
                productDates.add(wd.getDate());
                productCounts.add(wd.getCount());
            }
            request.setAttribute("productDates", productDates);
            request.setAttribute("productCounts", productCounts);

            // Weekly Revenue Data
            List<WeeklyData> weeklyRevenue = analyticsService.getWeeklyRevenue();
            List<String> revenueDates = new ArrayList<>();
            List<Integer> revenueAmounts = new ArrayList<>();
            for (WeeklyData wd : weeklyRevenue) {
                revenueDates.add(wd.getDate());
                revenueAmounts.add(wd.getCount());
            }
            request.setAttribute("revenueDates", revenueDates);
            request.setAttribute("revenueAmounts", revenueAmounts);

            ViewForwarder.forward(request, response, "admin/dashboard.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Admin dashboard", ex);
            throw new ServletException(ex);
        }
    }
}