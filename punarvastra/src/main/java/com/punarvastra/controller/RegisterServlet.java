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
 * New account registration (active immediately; no admin gate on login).
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(RegisterServlet.class.getName());
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Register");
        request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
        ViewForwarder.forward(request, response, "register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }
        try {
            User u = new User();
            u.setFullName(request.getParameter("fullName"));
            u.setUsername(request.getParameter("username"));
            u.setEmail(request.getParameter("email"));
            u.setPhone(request.getParameter("phone"));
            u.setAddress(request.getParameter("address"));
            String pw = request.getParameter("password");
            userService.register(u, pw);
            SessionUtil.setFlashSuccess(session, "Account created. You can sign in now.");
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception ex) {
            LOG.log(Level.FINE, "Register failed", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Registration failed.");
            response.sendRedirect(request.getContextPath() + "/register");
        }
    }
}
