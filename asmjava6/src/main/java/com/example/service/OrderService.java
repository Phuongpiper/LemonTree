package com.example.service;

import java.util.List;

import com.example.entity.Order;
import com.example.entity.OrderDTO;
import com.fasterxml.jackson.databind.JsonNode;

public interface OrderService {
     List<OrderDTO> getComplexOrders();
     List<Order> findAll();
     Order create(JsonNode orderData);

     void save(Order order);

     Order findById(Integer id);

     void setActiveForOrder(int id);
}
