package com.punarvastra.utils;

import java.util.Set;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern NAME_CHARS = Pattern.compile("^[A-Za-z][A-Za-z '\\-]{0,98}[A-Za-z']?$");
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_]{3,50}$");
    private static final Pattern EMAIL = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^9[0-9]{9}$");
    private static final Pattern PASSWORD = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$");
    private static final Set<String> IMAGE_EXT = Set.of("jpg", "jpeg", "png");

     private ValidationUtil(){

     }

     public static String validateFullName(String fullName){
         if (fullName == null || fullName.isBlank()) {
             return "Full name is required.";
         }
         String t = fullName.trim();
         if (t.matches(".*[0-9].*")) {
             return "Full name cannot contain numbers.";
         }
         if (t.length() < 2 || t.length() > 100) {
             return "Full name must be between 2 and 100 characters.";
         }
         if (!NAME_CHARS.matcher(t).matches()) {
             return "Full name may only contain letters, spaces, hyphens, and apostrophes.";
         }
         return null;
     }

     public static String validateUsername(String username){
         if (username == null || username.isBlank()) {
             return "Username is required.";
         }
         if (!USERNAME.matcher(username.trim()).matches()) {
             return "Username must be 3–50 characters (letters, digits, underscore only).";
         }
         return null;

     }

     public static String validateEmail(String email){
         if (email == null || email.isBlank()) {
             return "Email is required.";
         }
         if (!EMAIL.matcher(email.trim()).matches()) {
             return "Please enter a valid email address.";
         }
         return null;
     }


     public static String validatePhone(String phone){
         if (phone == null || phone.isBlank()) {
             return "Phone is required.";
         }
         String digits = phone.replaceAll("\\s+", "");
         if (!PHONE.matcher(digits).matches()) {
             return "Phone must be exactly 10 digits and start with 9.";
         }
         return null;
     }

    public static String validatePassword(String password){
        if (password == null || password.isBlank()) {
            return "Password is required.";
        }
        if (!PASSWORD.matcher(password).matches()) {
            return "Password must be at least 8 characters with 1 uppercase, 1 number, and 1 special character.";
        }
        return null;
    }

    public static String validateProductTitle(String title){
        if (title == null || title.isBlank()) {
            return "Title is required.";
        }
        if (title.trim().length() > 150) {
            return "Title must be at most 150 characters.";
        }
        return null;
    }

    public static String validateImageUpload(String filename, long sizeBytes){
        if (filename == null || filename.isBlank()) {
            return "Image file is required.";
        }
        int dot = filename.lastIndexOf('.');
        if (dot < 0) {
            return "Image must have an extension (.jpg, .jpeg, .png).";
        }
        String ext = filename.substring(dot + 1).toLowerCase();
        if (!IMAGE_EXT.contains(ext)) {
            return "Image must be JPG, JPEG, or PNG.";
        }
        if (sizeBytes > 5L * 1024 * 1024) {
            return "Image must be at most 5 MB.";
        }
        return null;
    }

    public static String validateStock(int stock){
        if (stock < 0) {
            return "Stock cannot be negative.";
        }
        return null;
    }
    }



