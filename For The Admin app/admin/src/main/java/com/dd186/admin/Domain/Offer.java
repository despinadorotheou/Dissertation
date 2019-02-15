package com.dd186.admin.Domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "offer_id", nullable = false)
    private int id;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "offer_product", joinColumns = @JoinColumn(name = "offer_id"), inverseJoinColumns = @JoinColumn(name = "productID"))
    private Set<Product> offerProducts;
    @Column(name = "offer_value")
    private double value;

    public Offer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Product> getOfferProducts() {
        return offerProducts;
    }

    public void setOfferProducts(Set<Product> offerProducts) {
        this.offerProducts = offerProducts;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
