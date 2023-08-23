package com.example.service;

import java.util.List;

import com.example.entity.Supplier;

public interface DistributorService {
    
    List<Supplier> findAll();
    Supplier create(Supplier supplier);
    Supplier findById(Integer id);
    void deleteById(Integer id);
    Supplier update(Supplier supplier);
}
