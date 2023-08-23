package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.entity.AddressCity;
import com.example.jparepository.AddressCityRepository;
import com.example.jparepository.AddressDistrictJpaRepository;
import com.example.service.AddressService;

public class CityServiceImpl implements AddressService {
	@Autowired
	private AddressCityRepository addressCityRepository;

	@Autowired
	private AddressDistrictJpaRepository addressDistrictRepository;

	public List<AddressCity> getAllCities() {
		return addressCityRepository.findAll();
	}
}
