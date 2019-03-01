package com.dd186.admin.Domain;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    public int id;
    @Column(name = "order_date")
    private Timestamp date;
    @Column(name = "order_value")
    private double value;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderProduct> orderProducts;

    public Order() {
    }

    public Order(@NonNull List<OrderProduct> orderProducts, double value) {
        for(OrderProduct orderProduct : orderProducts) orderProduct.setOrder(this);
        this.orderProducts = new HashSet<>(orderProducts);
        date = new Timestamp(System.currentTimeMillis());
        this.value = value;
    }

    public Order(double value) {
        this.value = value;
        date = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public List<OrderProduct> getOrderProducts() {
        return new ArrayList<>(orderProducts);
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        if (orderProducts == null)
            this.orderProducts = null;
        else
            this.orderProducts = new HashSet<>(orderProducts);
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


}
