package com.punarvastra.controller;

import com.punarvastra.entity.User;
import com.punarvastra.service.UserService;
import com.punarvastra.utils.CookieUtil;
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
 * Login form and credential check (BCrypt + optional remember-me).
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(LoginServlet.class.getName());
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Login");
        String remembered = CookieUtil.getRememberedUsername(request);
        request.setAttribute("rememberedUsername", remembered);
        request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
        ViewForwarder.forward(request, response, "login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("rememberMe");
        try {
            User u = userService.login(username, password);
            u.setPassword(null);
            SessionUtil.setCurrentUser(session, u);
            if ("on".equalsIgnoreCase(remember) || "true".equalsIgnoreCase(remember)) {
                CookieUtil.setRememberUsername(response, u.getUsername());
            } else {
                CookieUtil.clearRememberUsername(response);
            }
            Object redir = session.getAttribute("authRedirect");
            session.removeAttribute("authRedirect");
            if (redir instanceof String && !((String) redir).isBlank()) {
                String path = (String) redir;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                response.sendRedirect(request.getContextPath() + path);
                return;
            }
            if (u.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } catch (Exception ex) {
            LOG.log(Level.FINE, "Login failed", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Login failed.");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
