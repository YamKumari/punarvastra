package com.punarvastra.service;

import com.punarvastra.dao.InquiryDao;
import com.punarvastra.dao.InquiryDaoImpl;
import com.punarvastra.entity.Inquiry;
import com.punarvastra.exception.ValidationException;
import com.punarvastra.utils.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Contact form submissions.
 */
public class InquiryService {

    private final InquiryDao inquiryDao = new InquiryDaoImpl();

    /**
     * Persists a validated inquiry.
     */
    public void submit(Inquiry in) throws ValidationException, SQLException {
        String e = ValidationUtil.validateFullName(in.getName());
        if (e != null) {
            throw new ValidationException(e.replace("Full name", "Name"));
        }
        e = ValidationUtil.validateEmail(in.getEmail());
        if (e != null) {
            throw new ValidationException(e);
        }
        if (in.getSubject() == null || in.getSubject().isBlank()) {
            throw new ValidationException("Subject is required.");
        }
        if (in.getMessage() == null || in.getMessage().isBlank()) {
            throw new ValidationException("Message is required.");
        }
        inquiryDao.insert(in);
    }

    public List<Inquiry> listAll() throws SQLException {
        return inquiryDao.findAll();
    }

    public void markRead(int id) throws SQLException {
        inquiryDao.markRead(id);
    }

    public long countUnread() throws SQLException {
        return inquiryDao.countUnread();
    }
}
