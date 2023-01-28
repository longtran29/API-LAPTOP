package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.BrandEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

public class ProductDto {
    private String name;
    private String primaryImage;
    private String alias;
    private boolean enabled;
    private Float original_price;
    private Float discount_percent;

    private String brand;
    private String description;
    private boolean inStock;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public Float getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(Float original_price) {
        this.original_price = original_price;
    }

    public Float getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(Float discount_percent) {
        this.discount_percent = discount_percent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}
