package com.punarvastra.controller.service;

import com.punarvastra.dao.ProductDao;
import com.punarvastra.dao.ProductDaoImpl;
import com.punarvastra.entity.CartItem;
import com.punarvastra.entity.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.punarvastra.utils.SessionUtil.getCart;

public class CartService {

    private final ProductDao productDao = new ProductDaoImpl();

    /**
     * Adds or merges a product into the session cart.
     */
    public void add(HttpServletRequest request, int productId, int quantity) throws SQLException {
        if (quantity < 1) {
            quantity = 1;
        }
        Optional<Product> opt = productDao.findApprovedById(productId);
        if (opt.isEmpty()) {
            return;
        }
        Product p = opt.get();
        List<CartItem> cart = getCart(request);
        for (CartItem line : cart) {
            if (line.getProductId() == productId) {
                int next = Math.min(line.getQuantity() + quantity, p.getStock());
                line.setQuantity(next);
                line.setProduct(p);
                return;
            }
        }
        CartItem line = new CartItem(productId, Math.min(quantity, p.getStock()));
        line.setProduct(p);
        cart.add(line);
    }

    /**
     * Reloads product rows for every cart line (prices, stock, titles).
     */
    public void hydrate(HttpServletRequest request) throws SQLException {
        List<CartItem> cart = getCart(request);
        Iterator<CartItem> it = cart.iterator();
        while (it.hasNext()) {
            CartItem line = it.next();
            Optional<Product> opt = productDao.findApprovedById(line.getProductId());
            if (opt.isEmpty()) {
                it.remove();
                continue;
            }
            Product p = opt.get();
            line.setProduct(p);
            if (line.getQuantity() > p.getStock()) {
                line.setQuantity(Math.max(0, p.getStock()));
            }
            if (line.getQuantity() <= 0) {
                it.remove();
            }
        }
    }

    public void updateQty(HttpServletRequest request, int productId, int quantity) throws SQLException {
        List<CartItem> cart = getCart(request);
        for (CartItem line : cart) {
            if (line.getProductId() == productId) {
                Optional<Product> opt = productDao.findApprovedById(productId);
                if (opt.isEmpty()) {
                    cart.remove(line);
                    return;
                }
                int max = opt.get().getStock();
                line.setProduct(opt.get());
                line.setQuantity(Math.max(0, Math.min(quantity, max)));
                if (line.getQuantity() == 0) {
                    cart.remove(line);
                }
                return;
            }
        }
    }

    public void remove(HttpServletRequest request, int productId) {
        getCart(request).removeIf(l -> l.getProductId() == productId);
    }

    public int totalItems(HttpServletRequest request) {
        int n = 0;
        for (CartItem c : getCart(request)) {
            n += c.getQuantity();
        }
        return n;
    }

    public void clear(HttpServletRequest request) {
        getCart(request).clear();
    }


}
