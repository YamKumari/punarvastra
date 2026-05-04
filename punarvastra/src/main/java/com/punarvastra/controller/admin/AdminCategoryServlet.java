package com.punarvastra.controller.admin;

import com.punarvastra.controller.ViewForwarder;
import com.punarvastra.entity.Category;
import com.punarvastra.service.CategoryService;
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
 * Admin category CRUD.
 */
@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/categories"})
public class AdminCategoryServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AdminCategoryServlet.class.getName());
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("categories", categoryService.listAll());
            Category formCat = new Category();
            String eid = request.getParameter("edit");
            if (eid != null && !eid.isBlank()) {
                categoryService.findById(Integer.parseInt(eid)).ifPresent(c -> request.setAttribute("editCategory", c));
            }
            if (request.getAttribute("editCategory") == null) {
                request.setAttribute("editCategory", formCat);
            }
            request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
            ViewForwarder.forward(request, response, "admin/category-list.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Admin categories GET", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/admin/categories");
            return;
        }
        try {
            String action = request.getParameter("action");
            if ("save".equals(action)) {
                Category c = new Category();
                String id = request.getParameter("id");
                if (id != null && !id.isBlank()) {
                    c.setId(Integer.parseInt(id));
                }
                c.setName(request.getParameter("name"));
                c.setSlug(request.getParameter("slug"));
                c.setDescription(request.getParameter("description"));
                if (c.getId() != null) {
                    categoryService.update(c);
                } else {
                    categoryService.create(c);
                }
                SessionUtil.setFlashSuccess(session, "Category saved.");
            } else if ("delete".equals(action)) {
                categoryService.delete(Integer.parseInt(request.getParameter("id")));
                SessionUtil.setFlashSuccess(session, "Category deleted.");
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Admin categories POST", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Failed.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }
}
