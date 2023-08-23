package com.example.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "active", nullable = false)
    private boolean isActive;

    // Constructors, getters, setters, and other methods as needed.
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "addresscity_id")
    private AddressCity addressCity;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "addressdistrict_id")
    private AddressDistrict addressDistrict;
}
