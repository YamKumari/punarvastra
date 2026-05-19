package com.punarvastra.dao;

import com.punarvastra.entity.WeeklyData;
import com.punarvastra.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for analytics and chart data
 */
public class AnalyticsDao {

    /**
     * Get weekly orders count for last 7 days
     */
    public List<WeeklyData> getWeeklyOrdersCount() {
        List<WeeklyData> weeklyData = new ArrayList<>();
        String sql = "SELECT DATE(created_at) AS date, COUNT(*) AS count " +
                "FROM orders " +
                "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                "GROUP BY DATE(created_at) " +
                "ORDER BY date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                WeeklyData wd = new WeeklyData();
                wd.setDate(rs.getString("date"));
                wd.setCount(rs.getInt("count"));
                weeklyData.add(wd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeklyData;
    }

    /**
     * Get weekly user registrations for last 7 days
     */
    public List<WeeklyData> getWeeklyUserRegistrations() {
        List<WeeklyData> weeklyData = new ArrayList<>();
        String sql = "SELECT DATE(created_at) AS date, COUNT(*) AS count " +
                "FROM users " +
                "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                "GROUP BY DATE(created_at) " +
                "ORDER BY date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                WeeklyData wd = new WeeklyData();
                wd.setDate(rs.getString("date"));
                wd.setCount(rs.getInt("count"));
                weeklyData.add(wd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeklyData;
    }

    /**
     * Get weekly product listings for last 7 days
     */
    public List<WeeklyData> getWeeklyProductListings() {
        List<WeeklyData> weeklyData = new ArrayList<>();
        String sql = "SELECT DATE(created_at) AS date, COUNT(*) AS count " +
                "FROM products " +
                "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                "GROUP BY DATE(created_at) " +
                "ORDER BY date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                WeeklyData wd = new WeeklyData();
                wd.setDate(rs.getString("date"));
                wd.setCount(rs.getInt("count"));
                weeklyData.add(wd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeklyData;
    }

    /**
     * Get weekly revenue for last 7 days
     */
    public List<WeeklyData> getWeeklyRevenue() {
        List<WeeklyData> weeklyData = new ArrayList<>();
        String sql = "SELECT DATE(created_at) AS date, SUM(total_amount) AS count " +
                "FROM orders " +
                "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                "AND status = 'DELIVERED' " +
                "GROUP BY DATE(created_at) " +
                "ORDER BY date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                WeeklyData wd = new WeeklyData();
                wd.setDate(rs.getString("date"));
                wd.setCount(rs.getInt("count"));
                weeklyData.add(wd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeklyData;
    }
}