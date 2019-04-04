package com.dd186.admin.Services;

import com.dd186.admin.Domain.Order;
import com.dd186.admin.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

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

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

    public Order findById(int id){
        return orderRepository.findById(id);
    }

    public Order findlastorder(int userId){
        return orderRepository.findTopByUseridOrderByDateDesc(userId);
    }
}
