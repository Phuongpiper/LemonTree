package com.example.service;

import java.util.List;

import com.example.entity.Product;

public interface ProductService {

	Product findById(Integer id);

	List<Product> findAll();



	

}
