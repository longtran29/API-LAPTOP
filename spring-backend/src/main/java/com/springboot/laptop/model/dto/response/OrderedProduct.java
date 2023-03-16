package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.ProductEntity;

public class OrderedProduct {

    private ProductEntity product;
    private int quantity;

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
