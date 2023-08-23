package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Cart;
import com.example.jparepository.CartRepository;
import com.example.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private CartRepository cartDao;
	

	
	@Override
	public List<Cart> findAll() {
		return cartDao.findAll();
	}

	@Override
	public Cart findById(Integer id) {
		return cartDao.findById(id).get();
	}

	@Override
	public Cart create(Cart cart) {
		return cartDao.save(cart);
	}

	@Override
	public Cart update(Cart cart) {
		return cartDao.save(cart);
	}

	@Override
	public void deleteById(Integer id) {
		cartDao.deleteById(id);
	}


	@Override
	public List<Cart> findByUserId(Integer id) {
	    return cartDao.findByUserId(id);
	}

	

}
