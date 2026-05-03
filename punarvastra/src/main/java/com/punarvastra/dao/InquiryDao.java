package com.punarvastra.dao;

import com.punarvastra.entity.Inquiry;

import java.sql.SQLException;
import java.util.List;

/**
 * Contact inquiry persistence.
 */
public interface InquiryDao {

    void insert(Inquiry inquiry) throws SQLException;

    List<Inquiry> findAll() throws SQLException;

    void markRead(int id) throws SQLException;

    long countUnread() throws SQLException;
}
