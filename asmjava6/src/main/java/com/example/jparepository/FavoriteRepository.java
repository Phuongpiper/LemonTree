package com.example.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;
import com.example.entity.Favorite;
import com.example.entity.Product;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    // Các phương thức truy vấn dữ liệu cho Entity Favorite
    @Query(nativeQuery = true, value = "SELECT p.name, COUNT(f.product_id) AS like_count " +
            "FROM Favorites f " +
            "JOIN Products p ON p.id = f.product_id " +
            "GROUP BY p.name " +
            "ORDER BY like_count DESC")
    List<Object[]> fetchLikeCountByProduct();

    @Query(nativeQuery = true, value = "SELECT u.fullname " +
            "FROM Favorites f " +
            "JOIN Users u ON u.id = f.user_id " +
            "JOIN Products p ON p.id = f.product_id " +
            "WHERE p.name LIKE ?1")
    List<String> findUsersWhoLikedProduct(String nameProduct);

    Favorite findByAccountAndProduct(Account user, Product product);

    List<Favorite> findByAccount(Account user);

}
