package com.example.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

  @Query("SELECT o FROM Product o WHERE o.category.id LIKE ?1")
  List<Product> findByCategory(Integer integer);

  List<Product> findAll();

  Product findById(int id);
}
