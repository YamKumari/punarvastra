package com.punarvastra.controller.filter;

import com.punarvastra.entity.User;
import com.punarvastra.utils.SessionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Restricts {@code /admin} and nested paths to users with the ADMIN role.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin", "/admin/*"})
public class AdminFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        User u = SessionUtil.getCurrentUser(session);
        if (u == null || !u.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request, response);
    }
}
