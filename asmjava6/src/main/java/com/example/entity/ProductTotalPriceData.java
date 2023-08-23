package com.example.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductTotalPriceData {
    private String productName;
    private int count;
    private BigDecimal totalPrice;
}
