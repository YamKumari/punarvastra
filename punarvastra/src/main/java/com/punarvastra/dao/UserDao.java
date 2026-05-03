package com.punarvastra.dao;

import com.punarvastra.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Persistence operations for {@link User}.
 */
public interface UserDao {

    /**
     * Inserts a new user row.
     *
     * @param user populated entity (password must be hashed)
     * @return generated id
     */
    int insert(User user) throws SQLException, DuplicateRecordException;

    /**
     * Updates profile fields (not password).
     */
    void updateProfile(User user) throws SQLException;

    /**
     * Updates password hash.
     */
    void updatePassword(int userId, String bcryptHash) throws SQLException;

    /**
     * Sets approval flag (admin moderation).
     */
    void setApproved(int userId, boolean approved) throws SQLException;

    Optional<User> findById(int id) throws SQLException;

    Optional<User> findByUsername(String username) throws SQLException;

    Optional<User> findByEmail(String email) throws SQLException;

    Optional<User> findByPhoneDigits(String tenDigits) throws SQLException;

    List<User> findAll() throws SQLException;

    List<User> findPendingApprovals() throws SQLException;

    long countUsers() throws SQLException;

    long countPendingUsers() throws SQLException;
}
