package com.example.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    
}
