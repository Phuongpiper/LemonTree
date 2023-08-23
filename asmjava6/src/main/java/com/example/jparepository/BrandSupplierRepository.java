package com.example.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.BrandSupplier;

@Repository
public interface BrandSupplierRepository extends JpaRepository<BrandSupplier, Integer> {
    // Các phương thức truy vấn dữ liệu cho Entity BrandSupplier
}
