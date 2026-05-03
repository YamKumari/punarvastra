package com.punarvastra.controller.admin;

import com.punarvastra.controller.ViewForwarder;
import com.punarvastra.service.UserService;
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
 * Admin user list and account approval (optional legacy pending accounts).
 */
@WebServlet(name = "AdminUserServlet", urlPatterns = {"/admin/users"})
public class AdminUserServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AdminUserServlet.class.getName());
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("users", userService.listAllUsers());
            request.setAttribute("pendingUsers", userService.listPendingUsers());
            request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
            ViewForwarder.forward(request, response, "admin/user-list.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Admin users GET", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }
        try {
            int uid = Integer.parseInt(request.getParameter("userId"));
            boolean appr = "true".equalsIgnoreCase(request.getParameter("approved"));
            userService.setUserApproved(uid, appr);
            SessionUtil.setFlashSuccess(session, appr ? "User approved." : "User access revoked.");
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Admin users POST", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Failed.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
