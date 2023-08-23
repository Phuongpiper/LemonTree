package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.AddressDistrict;
import com.example.jparepository.AddressDistrictJpaRepository;
import com.example.service.AddressDistrictService;

@Service
public class AddressDistrictServiceImpl implements AddressDistrictService {
	@Autowired
	private AddressDistrictJpaRepository districtRepository;

	@Override
	public List<AddressDistrict> getAllDistricts() {
		// TODO Auto-generated method stub
		return districtRepository.findAll();
	}

}
