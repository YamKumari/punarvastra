package com.punarvastra.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * One persisted line on an order.
 */
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer orderId;
    private Integer productId;
    private int quantity;
    private BigDecimal priceAtPurchase;
    private String productTitle;
    private String productImage;

    public OrderItem() {
    }

    /**
     * @return line id
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return parent order id
     */
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * @return product id at time of purchase
     */
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * @return quantity ordered
     */
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return unit price snapshot
     */
    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    /**
     * @return denormalized title for receipts
     */
    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    /**
     * @return product image filename/path
     */
    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
