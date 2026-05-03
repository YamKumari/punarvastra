package com.punarvastra.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * One line in the shopping cart (session-backed).
 */
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int productId;
    private int quantity;
    /** Populated when rendering cart from service. */
    private Product product;

    public CartItem() {
    }

    /**
     * @param productId product primary key
     * @param quantity units (must be positive)
     */
    public CartItem(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * @return product id
     */
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return quantity in cart
     */
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return hydrated product or null until loaded
     */
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return line total using product price when available
     */
    public BigDecimal getLineTotal() {
        if (product == null || product.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
