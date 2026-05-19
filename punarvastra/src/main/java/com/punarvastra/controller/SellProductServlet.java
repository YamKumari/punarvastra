package com.punarvastra.controller;

import com.punarvastra.entity.Product;
import com.punarvastra.entity.User;
import com.punarvastra.service.CategoryService;
import com.punarvastra.service.ProductService;
import com.punarvastra.utils.ImageUtil;
import com.punarvastra.utils.SessionUtil;
import com.punarvastra.utils.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Seller flow: submit a new listing (pending admin approval).
 */
@WebServlet(name = "SellProductServlet", urlPatterns = {"/sell"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
        maxFileSize = 5 * 1024 * 1024,         // 5MB
        maxRequestSize = 6 * 1024 * 1024       // 6MB
)
public class SellProductServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SellProductServlet.class.getName());

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Sell");
            request.setAttribute("categories", categoryService.listAll());
            request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
            ViewForwarder.forward(request, response, "sell-product.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Sell GET", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        User u = SessionUtil.getCurrentUser(session);

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/sell");
            return;
        }

        try {
            Part filePart = request.getPart("image");
            String imageName = "placeholder.png";   // default image

            // Handle image upload
            if (filePart != null && filePart.getSize() > 0) {
                String submittedName = filePart.getSubmittedFileName();

                // Validate image
                String validationError = ValidationUtil.validateImageUpload(
                        submittedName != null ? submittedName : "x.jpg",
                        filePart.getSize()
                );

                if (validationError != null) {
                    SessionUtil.setFlashError(session, validationError);
                    response.sendRedirect(request.getContextPath() + "/sell");
                    return;
                }

                // Save image using new ImageUtil (external folder)
                imageName = ImageUtil.saveProductImage(filePart, submittedName);
            }

            // Create Product
            Product p = new Product();
            p.setSellerId(u.getId());
            p.setTitle(request.getParameter("title"));
            p.setDescription(request.getParameter("description"));
            p.setPrice(new BigDecimal(request.getParameter("price").trim()));
            p.setSize(request.getParameter("size"));
            p.setProductCondition(request.getParameter("productCondition"));
            p.setBrand(request.getParameter("brand"));
            p.setImage(imageName);
            p.setStock(Integer.parseInt(request.getParameter("stock").trim()));
            p.setCategoryId(Integer.parseInt(request.getParameter("categoryId").trim()));

            productService.createSellerListing(p);

            SessionUtil.setFlashSuccess(session,
                    "Listing submitted. It will appear on the shop after admin approval.");
            response.sendRedirect(request.getContextPath() + "/my-listings");

        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Sell POST", ex);
            SessionUtil.setFlashError(session,
                    ex.getMessage() != null ? ex.getMessage() : "Could not save listing.");
            response.sendRedirect(request.getContextPath() + "/sell");
        }
    }
}