package com.punarvastra.controller;

import com.punarvastra.entity.User;
import com.punarvastra.service.ProductService;
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

/**
 * Seller view of own listings (pending / approved / rejected) and resubmit.
 */
@WebServlet(name = "MyListingsServlet", urlPatterns = {"/my-listings"})
public class MyListingsServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(MyListingsServlet.class.getName());
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User u = SessionUtil.getCurrentUser(request.getSession(false));
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            request.setAttribute("pageTitle", "My listings");
            request.setAttribute("myProducts", productService.listSellerProducts(u.getId()));
            request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
            ViewForwarder.forward(request, response, "my-listings.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "My listings", ex);
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
            response.sendRedirect(request.getContextPath() + "/my-listings");
            return;
        }
        try {
            if ("resubmit".equals(request.getParameter("action"))) {
                int pid = Integer.parseInt(request.getParameter("productId"));
                productService.resubmitRejected(pid, u.getId());
                SessionUtil.setFlashSuccess(session, "Listing sent back for review.");
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "My listings POST", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Action failed.");
        }
        response.sendRedirect(request.getContextPath() + "/my-listings");
    }
}
