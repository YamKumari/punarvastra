package com.punarvastra.controller.admin;

import com.punarvastra.controller.ViewForwarder;
import com.punarvastra.entity.Category;
import com.punarvastra.entity.Product;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Admin product CRUD, image upload, and listing approval.
 */
@WebServlet(name = "AdminProductServlet", urlPatterns = {"/admin/products"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class AdminProductServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AdminProductServlet.class.getName());

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String status = request.getParameter("status");
            String st = (status == null || status.isBlank()) ? "APPROVED" : status.toUpperCase();
            request.setAttribute("products", productService.listAdminProducts(st));
            request.setAttribute("listStatus", st);

            List<Category> cats = categoryService.listAll();
            request.setAttribute("categories", cats);

            Product formBean = new Product();
            formBean.setListingStatus("APPROVED");
            formBean.setStock(1);
            formBean.setPrice(BigDecimal.ZERO);
            if (!cats.isEmpty()) {
                formBean.setCategoryId(cats.get(0).getId());
            }

            String editId = request.getParameter("edit");
            if (editId != null && !editId.isBlank()) {
                productService.getByIdAny(Integer.parseInt(editId)).ifPresent(p -> request.setAttribute("editProduct", p));
            }
            if (request.getAttribute("editProduct") == null) {
                request.setAttribute("editProduct", formBean);
            }

            request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
            ViewForwarder.forward(request, response, "admin/product-list.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Admin products GET", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        String action = request.getParameter("action");
        try {
            switch (action == null ? "" : action) {
                case "approve" -> {
                    int id = Integer.parseInt(request.getParameter("productId"));
                    productService.approveListing(id);
                    SessionUtil.setFlashSuccess(session, "Listing approved.");
                }
                case "reject" -> {
                    int id = Integer.parseInt(request.getParameter("productId"));
                    productService.rejectListing(id);
                    SessionUtil.setFlashSuccess(session, "Listing rejected.");
                }
                case "delete" -> {
                    int id = Integer.parseInt(request.getParameter("productId"));
                    productService.deleteProduct(id);
                    SessionUtil.setFlashSuccess(session, "Product deleted.");
                }
                case "save" -> saveProduct(request, session);
                default -> SessionUtil.setFlashError(session, "Unknown action.");
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Admin products POST", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Operation failed.");
        }

        String next = request.getParameter("nextStatus");
        String qs = (next != null && !next.isBlank()) ? "?status=" + java.net.URLEncoder.encode(next, java.nio.charset.StandardCharsets.UTF_8) : "";
        response.sendRedirect(request.getContextPath() + "/admin/products" + qs);
    }

    private void saveProduct(HttpServletRequest request, HttpSession session) throws Exception {
        Product p = new Product();
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isBlank()) {
            p.setId(Integer.parseInt(idStr));
        }

        p.setTitle(request.getParameter("title"));
        p.setDescription(request.getParameter("description"));
        p.setPrice(new BigDecimal(request.getParameter("price").trim()));
        p.setSize(request.getParameter("size"));
        p.setProductCondition(request.getParameter("productCondition"));
        p.setBrand(request.getParameter("brand"));
        p.setStock(Integer.parseInt(request.getParameter("stock").trim()));
        p.setCategoryId(Integer.parseInt(request.getParameter("categoryId").trim()));
        p.setListingStatus(request.getParameter("listingStatus"));

        Part part = request.getPart("image");
        String existingImage = request.getParameter("existingImage");
        String imageName = (existingImage != null && !existingImage.isBlank()) ? existingImage : "placeholder.png";

        // If new image is uploaded
        if (part != null && part.getSize() > 0) {
            String fn = part.getSubmittedFileName();
            String validationError = ValidationUtil.validateImageUpload(fn != null ? fn : "x.jpg", part.getSize());

            if (validationError != null) {
                throw new IllegalArgumentException(validationError);
            }

            // Delete old image if exists and is not placeholder
            if (existingImage != null && !existingImage.equals("placeholder.png")) {
                ImageUtil.deleteImage(existingImage);
            }

            // Save new image
            imageName = ImageUtil.saveProductImage(part, fn);
        }

        p.setImage(imageName);
        productService.saveAdminProduct(p);
        SessionUtil.setFlashSuccess(session, "Product saved successfully.");
    }
}