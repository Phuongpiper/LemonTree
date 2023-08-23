package com.example.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // Các phương thức truy vấn dữ liệu cho Entity OrderDetail
    @Query(nativeQuery = true, value = "SELECT o.id, o.adress, u.fullname, o.phone_number,o.date_order, SUM(p.price), STRING_AGG(p.name, ', '),o.wards,o.district,o.country "
            +
            "FROM Order_Details od " +
            "JOIN Orders o ON o.id = od.order_id " +
            "JOIN Users u ON u.id = o.user_id " +
            "JOIN Carts c ON c.id = od.cart_id " +
            "JOIN Products p ON p.id = c.product_id " +
            "GROUP BY o.id, o.adress, o.phone_number, u.fullname,o.date_order,o.wards,o.district,o.country")
    List<Object[]> getComplexOrders();

    @Query(nativeQuery = true, value = "SELECT YEAR(o.date_order) AS year, MONTH(o.date_order) AS month, SUM(p.price) AS total_revenue "
            +
            "FROM Order_Details od " +
            "JOIN Orders o ON o.id = od.order_id " +
            "JOIN Users u ON u.id = o.user_id " +
            "JOIN Carts c ON c.id = od.cart_id " +
            "JOIN Products p ON p.id = c.product_id " +
            "GROUP BY YEAR(o.date_order), MONTH(o.date_order) " +
            "ORDER BY year, month")
    List<Object[]> fetchRevenueData();

    @Query(nativeQuery = true, value = "SELECT p.name,count(p.price), SUM(p.price) AS total_price " +
            "FROM Order_Details od " +
            "JOIN Orders o ON o.id = od.order_id " +
            "JOIN Users u ON u.id = o.user_id " +
            "JOIN Carts c ON c.id = od.cart_id " +
            "JOIN Products p ON p.id = c.product_id " +
            "GROUP BY p.name ORDER BY total_price DESC")
    List<Object[]> fetchTotalPriceByProduct();
}
