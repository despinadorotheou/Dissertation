package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findById(int id);
    Order findTopByUseridOrderByDateDesc(int userId);
}
