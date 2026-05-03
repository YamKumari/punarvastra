package com.punarvastra.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Small helper to forward to JSPs under {@code WEB-INF/views}.
 */
public final class ViewForwarder {

    private ViewForwarder() {
    }

    /**
     * Forwards to {@code /WEB-INF/views/<relativePath>}.
     *
     * @param relativePath e.g. {@code home.jsp}
     */
    public static void forward(HttpServletRequest request, HttpServletResponse response, String relativePath)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/" + relativePath).forward(request, response);
    }
}
