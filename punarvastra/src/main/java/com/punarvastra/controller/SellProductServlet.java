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
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SellProductServlet", urlPatterns = {"/sell"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
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
        if (u == null || u.isAdmin()) {
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
            String submittedName = filePart != null ? filePart.getSubmittedFileName() : null;
            long size = filePart != null ? filePart.getSize() : 0;
            String imageName = "placeholder.png";
            if (filePart != null && size > 0) {
                String ve = ValidationUtil.validateImageUpload(submittedName != null ? submittedName : "x.jpg", size);
                if (ve != null) {
                    SessionUtil.setFlashError(session, ve);
                    response.sendRedirect(request.getContextPath() + "/sell");
                    return;
                }
                String real = getServletContext().getRealPath("/photos");
                if (real == null) {
                    throw new IOException("Cannot resolve /photos path.");
                }
                imageName = ImageUtil.saveProductImage(filePart, submittedName);
            }
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
