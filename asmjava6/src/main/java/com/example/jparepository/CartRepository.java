package com.example.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;
import com.example.entity.Cart;
import com.example.entity.Product;


@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	List<Cart> findByUserId(Integer id);
    // Các phương thức truy vấn dữ liệu cho Entity Cart
	Cart findByUser(Account user);
	Cart findByUserAndProduct(Account user, Product product);
	
}

