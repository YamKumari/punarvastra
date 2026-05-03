package com.punarvastra.dao;

import com.punarvastra.entity.Inquiry;
import com.punarvastra.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC {@link InquiryDao}.
 */
public class InquiryDaoImpl implements InquiryDao {

    private static Inquiry mapRow(ResultSet rs) throws SQLException {
        Inquiry i = new Inquiry();
        i.setId(rs.getInt("id"));
        i.setName(rs.getString("name"));
        i.setEmail(rs.getString("email"));
        i.setSubject(rs.getString("subject"));
        i.setMessage(rs.getString("message"));
        i.setRead(rs.getBoolean("is_read"));
        if (rs.getTimestamp("created_at") != null) {
            i.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return i;
    }

    @Override
    public void insert(Inquiry inquiry) throws SQLException {
        String sql = "INSERT INTO inquiries (name, email, subject, message) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inquiry.getName());
            ps.setString(2, inquiry.getEmail());
            ps.setString(3, inquiry.getSubject());
            ps.setString(4, inquiry.getMessage());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Inquiry> findAll() throws SQLException {
        List<Inquiry> list = new ArrayList<>();
        String sql = "SELECT * FROM inquiries ORDER BY created_at DESC";
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
    public void markRead(int id) throws SQLException {
        String sql = "UPDATE inquiries SET is_read=TRUE WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public long countUnread() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inquiries WHERE is_read=FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0;
        }
    }
}
