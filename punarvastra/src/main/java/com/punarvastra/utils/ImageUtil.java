package com.punarvastra.utils;

import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ImageUtil {

    private ImageUtil() {
    }

    /**
     * Writes an uploaded part to the photos directory.
     *
     * @param part        multipart file part
     * @param photosDir   absolute filesystem path to webapp/photos
     * @param originalName submitted filename (for extension)
     * @return stored filename only (not path)
     * @throws IOException if write fails
     */
    public static String saveProductImage(Part part, Path photosDir, String originalName) throws IOException {
        Files.createDirectories(photosDir);
        int dot = originalName != null ? originalName.lastIndexOf('.') : -1;
        String ext = dot > 0 ? originalName.substring(dot).toLowerCase() : ".jpg";
        if (!ext.matches("\\.(jpg|jpeg|png)$")) {
            ext = ".jpg";
        }
        String name = "pv_" + System.currentTimeMillis() + ext;
        Path target = photosDir.resolve(name);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return name;
    }
}
