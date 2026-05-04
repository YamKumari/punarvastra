package com.punarvastra.service;

import com.punarvastra.dao.ProductDao;
import com.punarvastra.dao.ProductDaoImpl;
import com.punarvastra.entity.Product;
import com.punarvastra.exception.ValidationException;
import com.punarvastra.utils.PriceUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Public catalogue, seller listings, and admin product operations.
 */
public class ProductService {

    private final ProductDao productDao = new ProductDaoImpl();

    /**
     * Public product grid with filters.
     */
    public List<Product> searchPublic(Integer categoryId, String size, String minPrice, String maxPrice,
                                      String condition, String sort, String keyword) throws SQLException {
        BigDecimal min = minPrice != null && !minPrice.isBlank() ? PriceUtil.parsePositive(minPrice) : null;
        BigDecimal max = maxPrice != null && !maxPrice.isBlank() ? PriceUtil.parsePositive(maxPrice) : null;
        return productDao.findPublicCatalog(categoryId, size, min, max, condition, sort, keyword);
    }

    public List<Product> featured(int limit) throws SQLException {
        return productDao.findFeaturedApproved(limit);
    }

    public Optional<Product> getApprovedProduct(int id) throws SQLException {
        return productDao.findApprovedById(id);
    }

    public Optional<Product> getByIdAny(int id) throws SQLException {
        return productDao.findById(id);
    }

    public List<Product> listSellerProducts(int sellerId) throws SQLException {
        return productDao.findBySeller(sellerId);
    }

    public List<Product> listByListingStatus(String status) throws SQLException {
        return productDao.findByListingStatus(status);
    }

    /**
     * @param status specific status or ALL
     */
    public List<Product> listAdminProducts(String status) throws SQLException {
        if (status != null && "ALL".equalsIgnoreCase(status)) {
            return productDao.findAllOrderByUpdated();
        }
        return productDao.findByListingStatus(status);
    }

    /**
     * Seller creates a new listing (pending moderation).
     */
    public int createSellerListing(Product p) throws ValidationException, SQLException {
        validateProduct(p);
        p.setListingStatus("PENDING");
        if (p.getSellerId() == null) {
            throw new ValidationException("Seller is required.");
        }
        return productDao.insert(p);
    }

    /**
     * Admin creates or seeds a product (optionally already approved).
     */
    public int saveAdminProduct(Product p) throws ValidationException, SQLException {
        validateProduct(p);
        if (p.getListingStatus() == null || p.getListingStatus().isBlank()) {
            p.setListingStatus("APPROVED");
        }
        if (p.getId() == null || p.getId() == 0) {
            return productDao.insert(p);
        }
        productDao.update(p);
        return p.getId();
    }

    public void deleteProduct(int id) throws SQLException {
        productDao.delete(id);
    }

    public void approveListing(int productId) throws SQLException {
        productDao.updateListingStatus(productId, "APPROVED");
    }

    public void rejectListing(int productId) throws SQLException {
        productDao.updateListingStatus(productId, "REJECTED");
    }

    public void resubmitRejected(int productId, int sellerId) throws SQLException {
        productDao.resubmitToPending(productId, sellerId);
    }

    public Optional<Product> findOwnedBySeller(int productId, int sellerId) throws SQLException {
        return productDao.findOwnedBySeller(productId, sellerId);
    }

    public long countApprovedProducts() throws SQLException {
        return productDao.countApproved();
    }

    public long countLowStock(int threshold) throws SQLException {
        return productDao.countLowStock(threshold);
    }

    private void validateProduct(Product p) throws ValidationException {
        String t = com.punarvastra.utils.ValidationUtil.validateProductTitle(p.getTitle());
        if (t != null) {
            throw new ValidationException(t);
        }
        if (p.getPrice() == null || p.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Valid price is required.");
        }
        String st = com.punarvastra.utils.ValidationUtil.validateStock(p.getStock());
        if (st != null) {
            throw new ValidationException(st);
        }
    }
}
