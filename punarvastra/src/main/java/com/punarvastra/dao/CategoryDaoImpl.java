package com.punarvastra.dao;

import com.punarvastra.entity.Category;
import com.punarvastra.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC {@link CategoryDao}.
 */
public class CategoryDaoImpl implements CategoryDao {

    private static Category mapRow(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setSlug(rs.getString("slug"));
        c.setDescription(rs.getString("description"));
        return c;
    }

    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Category> findById(int id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> findBySlug(String slug) throws SQLException {
        String sql = "SELECT * FROM categories WHERE slug=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slug.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void insert(Category c) throws SQLException {
        String sql = "INSERT INTO categories (name, slug, description) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getSlug());
            ps.setString(3, c.getDescription());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Category c) throws SQLException {
        String sql = "UPDATE categories SET name=?, slug=?, description=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getSlug());
            ps.setString(3, c.getDescription());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
