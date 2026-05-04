package com.punarvastra.controller.filter;

import com.punarvastra.service.CartService;
import com.punarvastra.service.CategoryService;
import com.punarvastra.service.WishlistService;
import com.punarvastra.utils.RequestPaths;
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
 * Attaches flash messages and cart / wishlist counts to every dynamic page request.
 */
@WebFilter(filterName = "UiBootstrapFilter", urlPatterns = "/*")
public class UiFilter extends HttpFilter {

    private final CartService cartService = new CartService();
    private final WishlistService wishlistService = new WishlistService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (RequestPaths.isStaticAsset(request)) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = request.getSession(true);
        SessionUtil.getOrCreateCsrfToken(session);
        String ok = SessionUtil.consumeFlashSuccess(session);
        String err = SessionUtil.consumeFlashError(session);
        if (ok != null) {
            request.setAttribute("flashSuccess", ok);
        }
        if (err != null) {
            request.setAttribute("flashError", err);
        }
        try {
            request.setAttribute("cartCount", cartService.totalItems(request));
            request.setAttribute("wishCount", wishlistService.count(request));
            request.setAttribute("navCategories", categoryService.listAll());
        } catch (Exception ex) {
            request.setAttribute("cartCount", 0);
            request.setAttribute("wishCount", 0);
        }
        chain.doFilter(request, response);
    }
}
