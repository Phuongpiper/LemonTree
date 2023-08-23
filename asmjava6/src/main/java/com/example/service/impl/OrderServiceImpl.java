package com.example.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Order;
import com.example.entity.OrderDTO;
import com.example.jparepository.OrderDetailRepository;
import com.example.jparepository.OrderRepository;
import com.example.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDetailRepository orderDetailRepository; // Assume you have this repository

    @Autowired
    OrderRepository OrderRepository;

    @Override
    public Order create(JsonNode orderData) {

        return null;
    }

    @Override
    public void save(Order order) {
        OrderRepository.save(order);

    }
    @Override
	public List<Order> findAll() {

		return OrderRepository.findAll();
	}

    @Override
    public List<OrderDTO> getComplexOrders() {
        List<Object[]> result = orderDetailRepository.getComplexOrders();
        System.out.println("Number of rows returned: " + result.size());
        List<OrderDTO> complexOrders = new ArrayList<>();
        for (Object[] row : result) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId((int) row[0]);
            orderDTO.setAdress((String) row[1]);
            orderDTO.setFullname((String) row[2]);
            orderDTO.setPhoneNumber((String) row[3]);
            orderDTO.setOrderDate((Timestamp) row[4]);
            orderDTO.setTotalPrice((BigDecimal) row[5]);
            orderDTO.setProductNames((String) row[6]);
            orderDTO.setWards((String) row[7]);
            orderDTO.setDistrict((String) row[8]);
            orderDTO.setCountry((String) row[9]);
            complexOrders.add(orderDTO);
        }
        return complexOrders;
    }

    @Override
    public Order findById(Integer orderId) {
        // TODO Auto-generated method stub
        return OrderRepository.findById(orderId).get();
    }

    @Override
    public void setActiveForOrder(int orderId) {
        Optional<Order> optionalOrder = OrderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setActive(true);
            OrderRepository.save(order);
        } else {
            // Xử lý trường hợp không tìm thấy đơn hàng với ID cụ thể
            // (ví dụ: thông báo lỗi, redirect, ...)
        }
    }

}
