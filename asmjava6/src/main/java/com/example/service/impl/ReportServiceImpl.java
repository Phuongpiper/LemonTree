package com.example.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.ProductTotalPriceData;
import com.example.entity.RevenueData;
import com.example.jparepository.OrderDetailRepository;
import com.example.service.ReportService;
@Service
public class ReportServiceImpl implements ReportService {
      @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<RevenueData> getRevenueDatas() {
        List<Object[]> result = orderDetailRepository.fetchRevenueData();
        List<RevenueData> revenueDataList = new ArrayList<>();

        for (Object[] row : result) {
            RevenueData revenueData = new RevenueData();
            revenueData.setYear((int) row[0]);
            revenueData.setMonth((int) row[1]);
            revenueData.setTotalRevenue((BigDecimal) row[2]);

            revenueDataList.add(revenueData);
        }

        return revenueDataList;
    }
    @Override
     public List<ProductTotalPriceData> getProductTotalPriceDatas() {
        List<Object[]> result = orderDetailRepository.fetchTotalPriceByProduct();
        List<ProductTotalPriceData> productTotalPriceDataList = new ArrayList<>();

        for (Object[] row : result) {
            ProductTotalPriceData productTotalPriceData = new ProductTotalPriceData();
            productTotalPriceData.setProductName((String) row[0]);
            productTotalPriceData.setCount((int) row[1]);
            productTotalPriceData.setTotalPrice( (BigDecimal) row[2]);

            productTotalPriceDataList.add(productTotalPriceData);
        }

        return productTotalPriceDataList;
    }
}
