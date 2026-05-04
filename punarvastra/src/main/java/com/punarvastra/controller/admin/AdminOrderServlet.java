package com.punarvastra.controller.admin;

import com.punarvastra.controller.ViewForwarder;
import com.punarvastra.service.OrderService;
import com.punarvastra.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Admin order list, detail, and status updates.
 */
@WebServlet(name = "AdminOrderServlet", urlPatterns = {"/admin/orders"})
public class AdminOrderServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AdminOrderServlet.class.getName());
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("id");
            if (id != null) {
                var opt = orderService.getOrderAdmin(Integer.parseInt(id));
                if (opt.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                request.setAttribute("order", opt.get());
                request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
                ViewForwarder.forward(request, response, "admin/order-detail.jsp");
                return;
            }
            String st = request.getParameter("status");
            request.setAttribute("orders", orderService.listOrdersForAdmin(st, 500));
            request.setAttribute("filterStatus", st);
            request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
            ViewForwarder.forward(request, response, "admin/order-list.jsp");
        } catch (NumberFormatException ex) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Admin orders GET", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }
        try {
            int oid = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");
            orderService.updateOrderStatus(oid, status);
            SessionUtil.setFlashSuccess(session, "Order status updated.");
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Admin orders POST", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Update failed.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
