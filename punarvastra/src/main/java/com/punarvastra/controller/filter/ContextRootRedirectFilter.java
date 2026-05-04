package com.punarvastra.controller.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Redirects {@code /punarvastra} to {@code /punarvastra/} so the welcome {@code index.jsp} runs reliably.
 */
@WebFilter(filterName = "ContextRootRedirectFilter", urlPatterns = "/*")
public class ContextRootRedirectFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        String ctx = request.getContextPath();
        if (ctx.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }
        String uri = request.getRequestURI();
        if (uri.equals(ctx)) {
            response.sendRedirect(ctx + "/");
            return;
        }
        chain.doFilter(request, response);
    }
}
