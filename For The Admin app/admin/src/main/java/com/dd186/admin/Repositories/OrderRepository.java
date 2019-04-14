package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findById(int id);
    Order findTopByUseridOrderByDateDesc(int userId);
}
