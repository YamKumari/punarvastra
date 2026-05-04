package com.punarvastra.controller;

import com.punarvastra.entity.Category;
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
 * Product grid with category, size, price, condition filters and sorting.
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ProductServlet.class.getName());

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Shop");

            String slug = request.getParameter("category");
            Integer catId = null;
            if (slug != null && !slug.isBlank()) {
                Category c = categoryService.findBySlug(slug).orElse(null);
                if (c != null) {
                    catId = c.getId();
                    request.setAttribute("activeCategory", c);
                }
            }

            String size = request.getParameter("size");
            String minP = request.getParameter("minPrice");
            String maxP = request.getParameter("maxPrice");
            String cond = request.getParameter("condition");
            String sort = request.getParameter("sort");
            String q = request.getParameter("q");

            // Fetch products
            request.setAttribute("products",
                    productService.searchPublic(catId, size, minP, maxP, cond, sort, q));

            request.setAttribute("categories", categoryService.listAll());
            request.setAttribute("filterSize", size);
            request.setAttribute("filterMin", minP);
            request.setAttribute("filterMax", maxP);
            request.setAttribute("filterCondition", cond);
            request.setAttribute("filterSort", sort);
            request.setAttribute("searchQ", q);

            ViewForwarder.forward(request, response, "product-list.jsp");

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Product list failed", ex);
            throw new ServletException(ex);
        }
    }
}