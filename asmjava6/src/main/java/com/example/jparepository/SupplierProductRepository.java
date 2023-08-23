package com.example.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.SupplierProduct;

@Repository
public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Integer> {
    // Các phương thức truy vấn dữ liệu cho Entity SupplierProduct
}
