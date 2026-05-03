package com.punarvastra.controller.admin;

import com.punarvastra.controller.ViewForwarder;
import com.punarvastra.service.InquiryService;
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
 * Admin view of contact inquiries.
 */
@WebServlet(name = "AdminInquiryServlet", urlPatterns = {"/admin/inquiries"})
public class AdminInquiryServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AdminInquiryServlet.class.getName());
    private final InquiryService inquiryService = new InquiryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("inquiries", inquiryService.listAll());
            request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
            ViewForwarder.forward(request, response, "admin/inquiry-list.jsp");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Admin inquiries GET", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/admin/inquiries");
            return;
        }
        try {
            inquiryService.markRead(Integer.parseInt(request.getParameter("id")));
            SessionUtil.setFlashSuccess(session, "Marked as read.");
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Admin inquiries POST", ex);
            SessionUtil.setFlashError(session, "Could not update inquiry.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/inquiries");
    }
}
