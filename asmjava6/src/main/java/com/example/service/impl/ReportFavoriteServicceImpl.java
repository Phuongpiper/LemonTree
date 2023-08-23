package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.LikeCountData;
import com.example.entity.UserLikedProduct;
import com.example.jparepository.FavoriteRepository;
import com.example.service.ReportFavoriteService;

@Service
public class ReportFavoriteServicceImpl implements ReportFavoriteService  {
    @Autowired
    FavoriteRepository favoriteRepository;

    @Override
public List<LikeCountData> getLikeCountData() {
    List<Object[]> result = favoriteRepository.fetchLikeCountByProduct();
    List<LikeCountData> likeCountDataList = new ArrayList<>();

    for (Object[] row : result) {
        LikeCountData likeCountData = new LikeCountData();
        likeCountData.setProductName((String) row[0]);
        likeCountData.setLikeCount((int) row[1]);

        likeCountDataList.add(likeCountData);
    }

    return likeCountDataList;
}
@Override
public List<UserLikedProduct> getUsersWhoLikedProduct(String productName) {
    List<String> usersWhoLiked = favoriteRepository.findUsersWhoLikedProduct(productName);
    List<UserLikedProduct> userLikedProducts = new ArrayList<>();

    for (String userName : usersWhoLiked) {
        UserLikedProduct userLikedProduct = new UserLikedProduct();
        userLikedProduct.setUserName(userName);
        userLikedProduct.setProductName(productName);

        userLikedProducts.add(userLikedProduct);
    }

    return userLikedProducts;
}


}
