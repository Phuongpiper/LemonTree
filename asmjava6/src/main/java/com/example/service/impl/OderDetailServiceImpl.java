package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.OrderDetail;
import com.example.service.OderDetailService;
import com.example.jparepository.OrderDetailRepository;
@Service
public class OderDetailServiceImpl implements OderDetailService{
	@Autowired
	OrderDetailRepository OrderDetailRepository;
	@Override
	public void save(OrderDetail detail) {
		OrderDetailRepository.save(detail);
		
	}

}
