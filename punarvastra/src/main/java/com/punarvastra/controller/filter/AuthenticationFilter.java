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
 * Requires an authenticated session for protected storefront routes.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/cart", "/checkout", "/orders", "/order", "/order-success", "/profile", "/wishlist", "/sell", "/my-listings"
})
public class AuthenticationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        User u = SessionUtil.getCurrentUser(session);
        if (u == null) {
            String ctx = request.getContextPath();
            String uri = request.getRequestURI();
            String after = uri.startsWith(ctx) ? uri.substring(ctx.length()) : uri;
            String qs = request.getQueryString();
            if (qs != null) {
                after += "?" + qs;
            }
            session = request.getSession(true);
            session.setAttribute("authRedirect", after);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
