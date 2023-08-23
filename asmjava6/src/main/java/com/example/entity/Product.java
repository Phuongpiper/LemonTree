package com.example.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @JoinColumn(name = "name")
    private String name;

    private String image;

    private String description;

    @NotNull(message = "{NotNull.Product.price}")
    @DecimalMin(value = "0.00", inclusive = false, message = "{DecimalMin.Product.price}")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "active")
    private boolean isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Cart> carts;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Discount> discounts;


    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories;

   

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<SupplierProduct> supplierProducts;

    // Constructors, getters, setters, and other methods as needed.
}
