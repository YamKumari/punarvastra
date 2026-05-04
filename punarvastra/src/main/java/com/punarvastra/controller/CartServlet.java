package com.punarvastra.controller;

import com.punarvastra.entity.CartItem;
import com.punarvastra.service.CartService;

import java.math.BigDecimal;
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
 * Session shopping cart (add, update quantity, remove).
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(CartServlet.class.getName());
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Cart");
            cartService.hydrate(request);
            var cart = SessionUtil.getCart(request);
            request.setAttribute("cart", cart);
            BigDecimal total = BigDecimal.ZERO;
            for (CartItem line : cart) {
                total = total.add(line.getLineTotal());
            }
            request.setAttribute("cartTotal", total);
            ViewForwarder.forward(request, response, "cart.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Cart GET failed", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token. Please try again.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                int pid = Integer.parseInt(request.getParameter("productId"));
                int qty = parseInt(request.getParameter("quantity"), 1);
                cartService.add(request, pid, qty);
                SessionUtil.setFlashSuccess(session, "Added to cart.");
                String redir = request.getParameter("redirect");
                if (redir != null && !redir.isBlank() && redir.startsWith("/")) {
                    response.sendRedirect(request.getContextPath() + redir);
                    return;
                }
            } else if ("update".equals(action)) {
                int pid = Integer.parseInt(request.getParameter("productId"));
                int qty = parseInt(request.getParameter("quantity"), 1);
                cartService.updateQty(request, pid, qty);
                SessionUtil.setFlashSuccess(session, "Cart updated.");
            } else if ("remove".equals(action)) {
                int pid = Integer.parseInt(request.getParameter("productId"));
                cartService.remove(request, pid);
                SessionUtil.setFlashSuccess(session, "Item removed.");
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Cart POST", ex);
            SessionUtil.setFlashError(session, "Could not update cart.");
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private static int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }
}
