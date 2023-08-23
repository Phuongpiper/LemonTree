package com.example.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RevenueData {
    private int id;

    private int year;
    private int month;
    private BigDecimal totalRevenue;
}
