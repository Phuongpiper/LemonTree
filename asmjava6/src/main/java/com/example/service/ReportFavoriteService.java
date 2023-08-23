package com.example.service;

import java.util.List;


import com.example.entity.LikeCountData;
import com.example.entity.UserLikedProduct;

public interface ReportFavoriteService {
    List<LikeCountData> getLikeCountData();
    List<UserLikedProduct> getUsersWhoLikedProduct(String productName);
}
