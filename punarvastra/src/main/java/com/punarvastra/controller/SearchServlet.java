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
 * Search results page (keyword query parameter {@code q}).
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SearchServlet.class.getName());
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Search");
            String q = request.getParameter("q");
            request.setAttribute("products", productService.searchPublic(null, null, null, null, null, "newest", q));
            request.setAttribute("categories", categoryService.listAll());
            request.setAttribute("searchQ", q);
            ViewForwarder.forward(request, response, "search-results.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Search failed", ex);
            throw new ServletException(ex);
        }
    }
}
