package com.example.service;

import java.util.List;

import com.example.entity.Cart;

public interface CartService {

	List<Cart> findAll();

	Cart findById(Integer id);

	Cart create(Cart cart);

	Cart update(Cart cart);

	void deleteById(Integer id);



	List<Cart> findByUserId(Integer id);



}
