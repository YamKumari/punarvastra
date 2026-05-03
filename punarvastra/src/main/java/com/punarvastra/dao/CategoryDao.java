package com.punarvastra.dao;

import com.punarvastra.entity.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Category persistence.
 */
public interface CategoryDao {

    List<Category> findAll() throws SQLException;

    Optional<Category> findById(int id) throws SQLException;

    Optional<Category> findBySlug(String slug) throws SQLException;

    void insert(Category c) throws SQLException;

    void update(Category c) throws SQLException;

    void delete(int id) throws SQLException;
}
