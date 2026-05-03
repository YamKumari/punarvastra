package com.punarvastra.controller;

import com.punarvastra.entity.User;
import com.punarvastra.service.CartService;
import com.punarvastra.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(CheckoutServlet.class.getName());
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Checkout");
            cartService.hydrate(request);
            if (SessionUtil.getCart(request).isEmpty()) {
                SessionUtil.setFlashError(request.getSession(), "Your cart is empty.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            User u = SessionUtil.getCurrentUser(request.getSession(false));
            request.setAttribute("profileAddress", u != null ? u.getAddress() : "");
            request.setAttribute("profilePhone", u != null ? u.getPhone() : "");
            request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
            ViewForwarder.forward(request, response, "checkout.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Checkout failed", ex);
            throw new ServletException(ex);
        }
    }
}
