package com.punarvastra.controller;

import com.punarvastra.entity.Inquiry;
import com.punarvastra.service.InquiryService;
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
 * Contact form backed by {@code inquiries} table.
 */
@WebServlet(name = "ContactServlet", urlPatterns = {"/contact"})
public class ContactServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ContactServlet.class.getName());
    private final InquiryService inquiryService = new InquiryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Contact");
        request.setAttribute("csrf", SessionUtil.getOrCreateCsrfToken(request.getSession(true)));
        ViewForwarder.forward(request, response, "contact.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        if (!SessionUtil.isValidCsrf(session, request.getParameter("csrf"))) {
            SessionUtil.setFlashError(session, "Invalid session token.");
            response.sendRedirect(request.getContextPath() + "/contact");
            return;
        }
        try {
            Inquiry in = new Inquiry();
            in.setName(request.getParameter("name"));
            in.setEmail(request.getParameter("email"));
            in.setSubject(request.getParameter("subject"));
            in.setMessage(request.getParameter("message"));
            inquiryService.submit(in);
            SessionUtil.setFlashSuccess(session, "Thank you — we will get back to you soon.");
        } catch (Exception ex) {
            LOG.log(Level.FINE, "Contact failed", ex);
            SessionUtil.setFlashError(session, ex.getMessage() != null ? ex.getMessage() : "Could not send message.");
        }
        response.sendRedirect(request.getContextPath() + "/contact");
    }
}