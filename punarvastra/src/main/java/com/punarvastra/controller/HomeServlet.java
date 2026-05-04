package com.punarvastra.controller;

import com.punarvastra.service.CategoryService;
import com.punarvastra.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Landing page with featured products and category highlights.
 * <p><strong>Do not map this servlet to {@code "/"}</strong> — that replaces Tomcat's default servlet and
 * requests like {@code /static/css/main.css} would run this servlet and return HTML instead of CSS.
 * The welcome {@code index.jsp} forwards here by servlet name.</p>
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(HomeServlet.class.getName());
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Home");
            request.setAttribute("featured", productService.featured(8));
            request.setAttribute("categories", categoryService.listAll());
            ViewForwarder.forward(request, response, "home.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Home failed", ex);
            throw new ServletException(ex);
        }
    }
}
