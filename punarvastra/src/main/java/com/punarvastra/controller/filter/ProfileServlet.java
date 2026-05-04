package com.punarvastra.controller;

import com.punarvastra.entity.User;
import com.punarvastra.service.UserService;
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
 * View/edit profile and change password.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ProfileServlet.class.getName());
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User u = SessionUtil.getCurrentUser(request.getSession(false));
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            request.setAttribute("pageTitle", "Profile");
            User fresh = userService.findById(u.getId()).orElse(u);
            fresh.setPassword(null);
            request.setAttribute("profileUser", fresh);
            request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
            ViewForwarder.forward(request, response, "profile.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Profile GET", ex);
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
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("profile".equals(action)) {
                u.setFullName(request.getParameter("fullName"));
                u.setPhone(request.getParameter("phone"));
                u.setAddress(request.getParameter("address"));
                userService.updateProfile(u);
                User re = userService.findById(u.getId()).orElse(u);
                re.setPassword(null);
                SessionUtil.setCurrentUser(session, re);
                SessionUtil.setFlashSuccess(session, "Profile updated.");
            } else if ("password".equals(action)) {
                userService.changePassword(u.getId(), request.getParameter("currentPassword"),
                        request.getParameter("newPassword"));
                SessionUtil.setFlashSuccess(session, "Password changed.");
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Profile POST", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Update failed.");
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}