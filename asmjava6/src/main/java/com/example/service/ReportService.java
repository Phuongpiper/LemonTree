package com.example.service;

import java.util.List;

import com.example.entity.ProductTotalPriceData;
import com.example.entity.RevenueData;


public interface ReportService {
     List<RevenueData> getRevenueDatas();
     List<ProductTotalPriceData> getProductTotalPriceDatas();
}
