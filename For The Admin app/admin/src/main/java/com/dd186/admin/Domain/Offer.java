package com.dd186.admin.Domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public List<Product> getOfferProducts() {
        return new ArrayList<>(offerProducts);
    }

    public void setOfferProducts(List<Product> offerProducts) {
        if (offerProducts == null)
            this.offerProducts = null;
        else
            this.offerProducts = new HashSet<>(offerProducts);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
