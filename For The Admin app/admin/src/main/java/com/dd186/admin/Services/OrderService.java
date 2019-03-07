package com.dd186.admin.Services;

import com.dd186.admin.Domain.Order;
import com.dd186.admin.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public void save(Order order){
        orderRepository.save(order);
    }
}
