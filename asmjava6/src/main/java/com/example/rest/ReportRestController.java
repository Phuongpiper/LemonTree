package com.example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.entity.LikeCountData;
import com.example.entity.ProductTotalPriceData;
import com.example.entity.RevenueData;
import com.example.entity.UserLikedProduct;
import com.example.service.ReportFavoriteService;
import com.example.service.ReportService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/Report")
public class ReportRestController {
    @Autowired
      ReportService reportService;
      @Autowired
      ReportFavoriteService favoriteService;
     @GetMapping()
    public List<RevenueData> getRevenueData() {
        return reportService.getRevenueDatas();
    }
     @GetMapping("/product")
    public List<ProductTotalPriceData> getProductTotalPriceData() {
        return reportService.getProductTotalPriceDatas();
    }
    @GetMapping("/Favorite")
    public List<LikeCountData> getAllFavorite() {
        return favoriteService.getLikeCountData();
    }
    @GetMapping("/UserFavorite/{productName}")
public List<UserLikedProduct> getUserFavorite(@PathVariable("productName") String productName) {
    return favoriteService.getUsersWhoLikedProduct(productName);
}
}
