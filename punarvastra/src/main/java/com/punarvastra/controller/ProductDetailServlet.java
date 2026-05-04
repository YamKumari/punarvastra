package com.punarvastra.controller;

import com.punarvastra.entity.Product;
import com.punarvastra.entity.User;
import com.punarvastra.service.ProductService;
import com.punarvastra.service.WishlistService;
import com.punarvastra.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Single product detail with wishlist state for the current session.
 */
@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product"})
public class ProductDetailServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ProductDetailServlet.class.getName());
    private final ProductService productService = new ProductService();
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("id");
            if (idStr == null) {
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }
            int id = Integer.parseInt(idStr);
            Optional<Product> opt = productService.getApprovedProduct(id);
            if (opt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            Product p = opt.get();
            request.setAttribute("pageTitle", p.getTitle());
            request.setAttribute("product", p);
            User u = SessionUtil.getCurrentUser(request.getSession(false));
            request.setAttribute("loggedIn", u != null);
            if (u != null) {
                request.setAttribute("inWishlist", wishlistService.contains(request, id));
            }
            ViewForwarder.forward(request, response, "product-detail.jsp");
        } catch (NumberFormatException ex) {
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Product detail failed", ex);
            throw new ServletException(ex);
        }
    }
}
