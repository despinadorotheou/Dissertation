package com.dd186.admin.Domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

import java.util.Set;

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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "order_product", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "productID"))
    private Set<Product> orderProducts;

    public Order() {
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

    public Set<Product> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(Set<Product> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Order(Timestamp date, double value, Set<Product> orderProducts) {
        this.date = date;
        this.value = value;
        this.orderProducts = orderProducts;
    }
}
