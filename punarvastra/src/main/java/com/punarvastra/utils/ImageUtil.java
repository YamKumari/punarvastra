package com.punarvastra.utils;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for handling product image uploads.
 * Images are saved outside the project in ~/punarvastra-uploads/
 * so they remain permanent even after rebuild or redeploy.
 */
public final class ImageUtil {

    private ImageUtil() {}

    /** External folder outside project */
    private static final String UPLOAD_DIR = System.getProperty("user.home")
            + File.separator + "punarvastra-uploads";

    /**
     * Saves uploaded product image with unique name.
     *
     * @param part         the uploaded file part
     * @param originalName original filename (to get extension)
     * @return stored filename (e.g. pv_20260502T104512_product.jpg)
     * @throws IOException if saving fails
     */
    public static String saveProductImage(Part part, String originalName) throws IOException {
        if (part == null || part.getSize() == 0) {
            return null;
        }

        // Create upload directory if not exists
        Path uploadPath = Path.of(UPLOAD_DIR);
        Files.createDirectories(uploadPath);

        // Validate and get extension
        String ext = ".jpg";
        if (originalName != null) {
            int dot = originalName.lastIndexOf('.');
            if (dot > 0) {
                String tempExt = originalName.substring(dot).toLowerCase();
                if (tempExt.matches("\\.(jpg|jpeg|png)$")) {
                    ext = tempExt;
                }
            }
        }

        // Generate unique filename
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        String fileName = "pv_" + timestamp + ext;

        Path target = uploadPath.resolve(fileName);

        // Save file
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    /**
     * Deletes an old image when updating a product.
     */
    public static void deleteImage(String fileName) {
        if (fileName == null || fileName.isEmpty()) return;

        Path filePath = Path.of(UPLOAD_DIR, fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}