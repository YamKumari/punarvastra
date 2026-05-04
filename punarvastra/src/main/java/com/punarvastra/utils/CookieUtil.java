package com.punarvastra.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Remember-me username cookie (7 days, HTTP-only).
 */
public final class CookieUtil {

    public static final String REMEMBER_USER = "pv_remember_user";
    private static final int MAX_AGE_SEC = 7 * 24 * 60 * 60;
    private static final String PATH = "/";

    private CookieUtil() {
    }

    /**
     * Stores username in a long-lived cookie.
     *
     * @param response servlet response
     * @param username login name
     */
    public static void setRememberUsername(HttpServletResponse response, String username) {
        Cookie c = new Cookie(REMEMBER_USER, username != null ? username : "");
        c.setMaxAge(MAX_AGE_SEC);
        c.setPath(PATH);
        c.setHttpOnly(true);
        response.addCookie(c);
    }

    /**
     * Clears the remember-me cookie.
     *
     * @param response servlet response
     */
    public static void clearRememberUsername(HttpServletResponse response) {
        Cookie c = new Cookie(REMEMBER_USER, "");
        c.setMaxAge(0);
        c.setPath(PATH);
        c.setHttpOnly(true);
        response.addCookie(c);
    }

    /**
     * Reads remembered username if present.
     *
     * @param request HTTP request
     * @return username or null
     */
    public static String getRememberedUsername(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie c : request.getCookies()) {
            if (REMEMBER_USER.equals(c.getName()) && c.getValue() != null && !c.getValue().isBlank()) {
                return c.getValue();
            }
        }
        return null;
    }
}
