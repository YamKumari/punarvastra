package com.punarvastra.service;

import com.punarvastra.dao.ProductDao;
import com.punarvastra.dao.ProductDaoImpl;
import com.punarvastra.entity.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.punarvastra.utils.SessionUtil.getWishlist;

public class WishlistService {
    private final ProductDao productDao = new ProductDaoImpl();

    /**
     * Adds id if product is approved and in stock.
     */
    public boolean add(HttpServletRequest request, int productId) throws SQLException {
        Optional<Product> opt = productDao.findApprovedById(productId);
        if (opt.isEmpty()) {
            return false;
        }
        Set<Integer> w = getWishlist(request);
        return w.add(productId);
    }

    public void remove(HttpServletRequest request, int productId) {
        getWishlist(request).remove(productId);
    }

    public boolean contains(HttpServletRequest request, int productId) {
        return getWishlist(request).contains(productId);
    }

    public int count(HttpServletRequest request) {
        return getWishlist(request).size();
    }

    /**
     * @return products still available (approved + stock), preserves order
     */
    public List<Product> hydrate(HttpServletRequest request) throws SQLException {
        List<Product> out = new ArrayList<>();
        for (Integer id : getWishlist(request)) {
            Optional<Product> opt = productDao.findApprovedById(id);
            opt.ifPresent(out::add);
        }
        return out;
    }


}
