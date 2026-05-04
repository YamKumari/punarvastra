package com.punarvastra.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Path helpers for filters (servletPath vs pathInfo differs for default vs named mappings).
 */
public final class RequestPaths {

    private RequestPaths() {
    }

    /**
     * Path after the context path, always starting with {@code '/'} (or empty if at context root).
     */
    public static String pathAfterContext(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        if (ctx != null && !ctx.isEmpty() && uri.startsWith(ctx)) {
            return uri.substring(ctx.length());
        }
        return uri != null ? uri : "/";
    }

    /**
     * Static assets served by Tomcat's default mechanism (not JSP/servlets).
     */
    public static boolean isStaticAsset(HttpServletRequest request) {
        String p = pathAfterContext(request);
        return p.startsWith("/static/") || p.startsWith("/photos/");
    }
}
