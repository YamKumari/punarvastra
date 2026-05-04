package com.punarvastra.controller;

import com.punarvastra.utils.CookieUtil;
import com.punarvastra.utils.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Invalidates session and clears remember-me cookie.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession s = request.getSession(false);
        if (s != null) {
            s.invalidate();
        }
        CookieUtil.clearRememberUsername(response);
        HttpSession ns = request.getSession(true);
        SessionUtil.setFlashSuccess(ns, "You have been logged out.");
        response.sendRedirect(request.getContextPath() + "/");
    }
}
