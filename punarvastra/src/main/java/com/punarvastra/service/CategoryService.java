package com.punarvastra.service;

import com.punarvastra.dao.CategoryDao;
import com.punarvastra.dao.CategoryDaoImpl;
import com.punarvastra.entity.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Category catalogue and admin CRUD.
 */
public class CategoryService {

    private final CategoryDao categoryDao = new CategoryDaoImpl();

    /**
     * @return all categories ordered by name
     */
    public List<Category> listAll() throws SQLException {
        return categoryDao.findAll();
    }

    /**
     * Resolves category by URL slug.
     */
    public Optional<Category> findBySlug(String slug) throws SQLException {
        if (slug == null || slug.isBlank()) {
            return Optional.empty();
        }
        return categoryDao.findBySlug(slug.trim().toLowerCase(Locale.ROOT));
    }

    public Optional<Category> findById(int id) throws SQLException {
        return categoryDao.findById(id);
    }

    /**
     * Validates and inserts a category.
     */
    public void create(Category c) throws ValidationException, SQLException {
        validate(c);
        categoryDao.insert(c);
    }

    /**
     * Validates and updates a category.
     */
    public void update(Category c) throws ValidationException, SQLException {
        validate(c);
        categoryDao.update(c);
    }

    public void delete(int id) throws SQLException {
        categoryDao.delete(id);
    }

    private void validate(Category c) throws ValidationException {
        if (c.getName() == null || c.getName().isBlank()) {
            throw new ValidationException("Category name is required.");
        }
        if (c.getSlug() == null || c.getSlug().isBlank()) {
            throw new ValidationException("Category slug is required.");
        }
    }
}
