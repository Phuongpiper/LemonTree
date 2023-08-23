package com.example.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class OrderDTO {
    private int id;
    private String adress;
    private String fullname;
    private String phoneNumber;
    private Timestamp orderDate;
    private BigDecimal totalPrice;
    private String productNames;
    private String wards;
    private String district;
    private String country;
    // Getters and setters
}

