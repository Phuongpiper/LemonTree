package com.example.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AddressDistrict")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDistrict implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "addressdistrict_id")
	private int addressdistrict_id;

	@Column(name = "name")
	private String name;

	// Getters and Setters
}
