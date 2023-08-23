package com.example.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    // Các phương thức truy vấn dữ liệu cho Entity Discount
}
