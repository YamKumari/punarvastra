package com.punarvastra.dao;

import com.punarvastra.entity.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Product catalogue and moderation persistence.
 */
public interface ProductDao {

    List<Product> findPublicCatalog(Integer categoryId, String size, BigDecimal minPrice,
                                    BigDecimal maxPrice, String condition, String sort, String keyword)
            throws SQLException;

    Optional<Product> findApprovedById(int id) throws SQLException;

    Optional<Product> findById(int id) throws SQLException;

    Optional<Product> findOwnedBySeller(int productId, int sellerId) throws SQLException;

    List<Product> findFeaturedApproved(int limit) throws SQLException;

    List<Product> findBySeller(int sellerId) throws SQLException;

    List<Product> findByListingStatus(String listingStatus) throws SQLException;

    /** All products for admin catalogue (any status). */
    List<Product> findAllOrderByUpdated() throws SQLException;

    int insert(Product p) throws SQLException;

    void update(Product p) throws SQLException;

    void updateListingStatus(int productId, String status) throws SQLException;

    void resubmitToPending(int productId, int sellerId) throws SQLException;

    void delete(int id) throws SQLException;

    long countApproved() throws SQLException;

    long countAll() throws SQLException;

    long countByListingStatus(String listingStatus) throws SQLException;

    long countLowStock(int threshold) throws SQLException;

    /**
     * Decrements stock if enough units (single statement).
     *
     * @return rows updated (1 if success)
     */
    int decrementStock(Connection conn, int productId, int quantity) throws SQLException;
}
