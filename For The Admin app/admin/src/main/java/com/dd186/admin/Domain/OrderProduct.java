package com.dd186.admin.Domain;

import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class OrderProduct implements Serializable{

    @Id
    @ManyToOne
    @JoinColumn
    private Order order;
    @Id
    @ManyToOne
    @JoinColumn
    private Product product;

    private int quantity;

    public OrderProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderProduct() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(order.getId(), product.getId(), quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderProduct)) return false;
        OrderProduct that = (OrderProduct) obj;
        return Objects.equals(order.getId(), that.order.getId()) &&
                Objects.equals(product.getId(), that.product.getId()) &&
                Objects.equals(quantity, that.quantity);
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
