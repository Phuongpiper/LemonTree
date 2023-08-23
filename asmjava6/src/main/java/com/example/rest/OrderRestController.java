package com.example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.AddressDistrict;
import com.example.entity.Order;
import com.example.entity.OrderDTO;
import com.example.jparepository.AddressDistrictJpaRepository;
import com.example.service.OrderService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {
	private final AddressDistrictJpaRepository addressDistrictRepository;
	

    @GetMapping("/all")
    public List<Order> getAll() {
        return orderService.findAll();
    }
	@Autowired
	public OrderRestController(AddressDistrictJpaRepository addressDistrictRepository) {
		this.addressDistrictRepository = addressDistrictRepository;
	}

	@GetMapping
	public List<AddressDistrict> getAllDistricts() {
		List<AddressDistrict> districts = addressDistrictRepository.findAll();
		for (AddressDistrict district : districts) {
			System.out.println("District name: " + district.getName());
		}
		return districts;
	}

	@Autowired
	OrderService orderService;

	@GetMapping("/complex")
	public List<OrderDTO> getOrderDetailsWithProductNames() {

		return orderService.getComplexOrders();
	}

}
