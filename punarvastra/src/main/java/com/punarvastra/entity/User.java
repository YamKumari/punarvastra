package com.punarvastra.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Application user (buyer, seller, or administrator).
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role;
    private boolean approved;
    private LocalDateTime createdAt;

    /** Default constructor for frameworks and JSP beans. */
    public User() {
    }

    /**
     * @return primary key or null if not persisted
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id database id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return display name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName legal or preferred name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return login username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username unique login
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email unique email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return BCrypt hash (never expose in JSP)
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password BCrypt hash
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Nepal mobile digits
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone 10-digit mobile
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return shipping or profile address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address free-text address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return USER or ADMIN
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role USER or ADMIN
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return true if administrator may allow restricted actions
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * @param approved admin approval flag
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * @return registration timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt registration time
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return true if this user has the ADMIN role
     */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}
