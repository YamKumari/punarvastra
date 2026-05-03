package com.punarvastra.controller;

import com.punarvastra.service.CartService;
import com.punarvastra.service.WishlistService;
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

@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist"})
public class WishlistServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(WishlistServlet.class.getName());
    private final WishlistService wishlistService = new WishlistService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Wishlist");
            request.setAttribute("wishlistProducts", wishlistService.hydrate(request));
            ViewForwarder.forward(request, response, "wishlist.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Wishlist GET failed", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/wishlist");
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                int pid = Integer.parseInt(request.getParameter("productId"));
                if (wishlistService.add(request, pid)) {
                    SessionUtil.setFlashSuccess(session, "Saved to wishlist.");
                } else {
                    SessionUtil.setFlashError(session, "Could not add (sold out or unavailable).");
                }
                String redir = request.getParameter("redirect");
                if (redir != null && !redir.isBlank() && redir.startsWith("/")) {
                    response.sendRedirect(request.getContextPath() + redir);
                    return;
                }
            } else if ("remove".equals(action)) {
                int pid = Integer.parseInt(request.getParameter("productId"));
                wishlistService.remove(request, pid);
                SessionUtil.setFlashSuccess(session, "Removed from wishlist.");
            } else if ("toCart".equals(action)) {
                int pid = Integer.parseInt(request.getParameter("productId"));
                cartService.add(request, pid, 1);
                wishlistService.remove(request, pid);
                SessionUtil.setFlashSuccess(session, "Moved to cart.");
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Wishlist POST", ex);
            SessionUtil.setFlashError(session, "Wishlist action failed.");
        }
        response.sendRedirect(request.getContextPath() + "/wishlist");
    }
}
