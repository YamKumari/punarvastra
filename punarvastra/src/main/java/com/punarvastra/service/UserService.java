package com.punarvastra.service;

import com.punarvastra.dao.UserDao;
import com.punarvastra.dao.UserDaoImpl;
import com.punarvastra.entity.User;
import com.punarvastra.exception.DuplicateRecordException;
import com.punarvastra.exception.UserNotFoundException;
import com.punarvastra.exception.ValidationException;
import com.punarvastra.utils.PasswordUtil;
import com.punarvastra.utils.ValidationUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Registration, authentication, profile, and admin user moderation.
 */
public class UserService {

    private final UserDao userDao = new UserDaoImpl();

    /**
     * Registers a new buyer/seller account (immediately approved for login).
     *
     * @param user plaintext password on input; replaced with hash before insert
     * @return new user id
     */
    public int register(User user, String plainPassword) throws ValidationException, DuplicateRecordException, SQLException {
        String e = ValidationUtil.validateFullName(user.getFullName());
        if (e != null) {
            throw new ValidationException(e);
        }
        e = ValidationUtil.validateUsername(user.getUsername());
        if (e != null) {
            throw new ValidationException(e);
        }
        e = ValidationUtil.validateEmail(user.getEmail());
        if (e != null) {
            throw new ValidationException(e);
        }
        e = ValidationUtil.validatePhone(user.getPhone());
        if (e != null) {
            throw new ValidationException(e);
        }
        e = ValidationUtil.validatePassword(plainPassword);
        if (e != null) {
            throw new ValidationException(e);
        }
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setUsername(user.getUsername().trim());
        user.setPhone(user.getPhone().replaceAll("\\s+", ""));
        user.setPassword(PasswordUtil.hashPassword(plainPassword));
        user.setRole("USER");
        user.setApproved(true);
        return userDao.insert(user);
    }

    /**
     * Authenticates username + password.
     *
     * @return loaded user without password field cleared (caller should avoid exposing)
     */
    public User login(String username, String password) throws UserNotFoundException, ValidationException, SQLException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new ValidationException("Username and password are required.");
        }
        Optional<User> opt = userDao.findByUsername(username.trim());
        if (opt.isEmpty()) {
            throw new UserNotFoundException("Invalid credentials.");
        }
        User u = opt.get();
        if (!u.isApproved()) {
            throw new UserNotFoundException("Your account is pending administrator approval.");
        }
        if (!PasswordUtil.checkPassword(password, u.getPassword())) {
            throw new UserNotFoundException("Invalid credentials.");
        }
        return u;
    }

    /**
     * Updates profile fields for an existing user.
     */
    public void updateProfile(User user) throws ValidationException, SQLException {
        String e = ValidationUtil.validateFullName(user.getFullName());
        if (e != null) {
            throw new ValidationException(e);
        }
        e = ValidationUtil.validatePhone(user.getPhone());
        if (e != null) {
            throw new ValidationException(e);
        }
        user.setPhone(user.getPhone().replaceAll("\\s+", ""));
        userDao.updateProfile(user);
    }

    /**
     * Changes password after verifying the current password.
     */
    public void changePassword(int userId, String currentPlain, String newPlain)
            throws ValidationException, UserNotFoundException, SQLException {
        String ev = ValidationUtil.validatePassword(newPlain);
        if (ev != null) {
            throw new ValidationException(ev);
        }
        User u = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        if (!PasswordUtil.checkPassword(currentPlain, u.getPassword())) {
            throw new ValidationException("Current password is incorrect.");
        }
        userDao.updatePassword(userId, PasswordUtil.hashPassword(newPlain));
    }

    /**
     * @return all users for admin list
     */
    public List<User> listAllUsers() throws SQLException {
        return userDao.findAll();
    }

    /**
     * @return users awaiting account approval
     */
    public List<User> listPendingUsers() throws SQLException {
        return userDao.findPendingApprovals();
    }

    /**
     * Approves or blocks a user account.
     */
    public void setUserApproved(int userId, boolean approved) throws SQLException {
        userDao.setApproved(userId, approved);
    }

    public Optional<User> findById(int id) throws SQLException {
        return userDao.findById(id);
    }

    public long countUsers() throws SQLException {
        return userDao.countUsers();
    }
}
