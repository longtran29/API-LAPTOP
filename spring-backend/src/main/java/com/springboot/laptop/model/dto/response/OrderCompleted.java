package com.springboot.laptop.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalIdCache;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCompleted {

    private List<OrderedProduct> orderedProducts;
    private Address deliveryAddress;

    private float totalAmt;
    private float cartTotal;

    private String email;

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }


}
