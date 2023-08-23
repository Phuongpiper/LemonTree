package com.example.service;

import java.util.List;

import com.example.entity.Brand;

public interface BrandService {
    List<Brand> findAll();
    Brand create(Brand brand);
    Brand findById(Integer id);
    void deleteById(Integer id);
    Brand update(Brand brand);
    
}