package com.punarvastra.utils;

import com.punarvastra.entity.CartItem;
import com.punarvastra.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Typed access to session attributes (user, cart, wishlist, flash, CSRF).
 */
public final class SessionUtil {

    public static final String ATTR_USER = "currentUser";
    public static final String ATTR_CART = "cartItems";
    public static final String ATTR_WISHLIST = "wishlistProductIds";
    public static final String FLASH_SUCCESS = "flashSuccess";
    public static final String FLASH_ERROR = "flashError";
    public static final String ATTR_CSRF = "csrfToken";

    private SessionUtil() {
    }

    /**
     * @param session HTTP session
     * @return logged-in user or null
     */
    public static User getCurrentUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object u = session.getAttribute(ATTR_USER);
        return u instanceof User ? (User) u : null;
    }

    /**
     * @param session HTTP session
     * @param user    authenticated user
     */
    public static void setCurrentUser(HttpSession session, User user) {
        session.setAttribute(ATTR_USER, user);
    }

    /**
     * @param request current request
     * @return mutable cart list (never null)
     */
    @SuppressWarnings("unchecked")
    public static List<CartItem> getCart(HttpServletRequest request) {
        HttpSession s = request.getSession(true);
        Object c = s.getAttribute(ATTR_CART);
        if (!(c instanceof List)) {
            List<CartItem> list = new ArrayList<>();
            s.setAttribute(ATTR_CART, list);
            return list;
        }
        return (List<CartItem>) c;
    }

    /**
     * @param request current request
     * @return ordered unique wishlist product ids
     */
    @SuppressWarnings("unchecked")
    public static Set<Integer> getWishlist(HttpServletRequest request) {
        HttpSession s = request.getSession(true);
        Object w = s.getAttribute(ATTR_WISHLIST);
        if (!(w instanceof Set)) {
            Set<Integer> set = new LinkedHashSet<>();
            s.setAttribute(ATTR_WISHLIST, set);
            return set;
        }
        return (Set<Integer>) w;
    }

    /**
     * Sets a one-shot success message.
     *
     * @param session HTTP session
     * @param message user-visible text
     */
    public static void setFlashSuccess(HttpSession session, String message) {
        session.setAttribute(FLASH_SUCCESS, message);
    }

    /**
     * Sets a one-shot error message.
     *
     * @param session HTTP session
     * @param message user-visible text
     */
    public static void setFlashError(HttpSession session, String message) {
        session.setAttribute(FLASH_ERROR, message);
    }

    /**
     * Reads and removes flash success.
     *
     * @param session HTTP session
     * @return message or null
     */
    public static String consumeFlashSuccess(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object m = session.getAttribute(FLASH_SUCCESS);
        session.removeAttribute(FLASH_SUCCESS);
        return m != null ? m.toString() : null;
    }

    /**
     * Reads and removes flash error.
     *
     * @param session HTTP session
     * @return message or null
     */
    public static String consumeFlashError(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object m = session.getAttribute(FLASH_ERROR);
        session.removeAttribute(FLASH_ERROR);
        return m != null ? m.toString() : null;
    }

    /**
     * Ensures a CSRF token exists in session and returns it.
     *
     * @param session HTTP session
     * @return token string
     */
    public static String getOrCreateCsrfToken(HttpSession session) {
        Object t = session.getAttribute(ATTR_CSRF);
        if (t instanceof String && !((String) t).isBlank()) {
            return (String) t;
        }
        String n = java.util.UUID.randomUUID().toString();
        session.setAttribute(ATTR_CSRF, n);
        return n;
    }

    /**
     * Validates POST CSRF token against session.
     *
     * @param session HTTP session
     * @param token   form token (may be null)
     * @return true if valid
     */
    public static boolean isValidCsrf(HttpSession session, String token) {
        if (session == null || token == null || token.isBlank()) {
            return false;
        }
        Object s = session.getAttribute(ATTR_CSRF);
        return s != null && token.equals(s);
    }
}
