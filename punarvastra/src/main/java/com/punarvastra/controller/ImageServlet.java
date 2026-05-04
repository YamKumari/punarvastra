package com.punarvastra.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {
    private static final String UPLOAD_DIR = System.getProperty("user.home")
            + File.separator + "punarvastra-uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String fileName = pathInfo.substring(1);
        Path filePath = Path.of(UPLOAD_DIR, fileName);

        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Security check
        if (!filePath.toAbsolutePath().startsWith(Path.of(UPLOAD_DIR).toAbsolutePath())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String contentType = Files.probeContentType(filePath);
        response.setContentType(contentType != null ? contentType : "image/jpeg");

        Files.copy(filePath, response.getOutputStream());
    }
}
