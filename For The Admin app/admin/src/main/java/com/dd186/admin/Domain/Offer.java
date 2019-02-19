package com.dd186.admin.Domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "offer_id", nullable = false)
    private int id;
    @Column(name = "offer_description", nullable = false)
    private String description;
    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OfferProduct> offerProducts;
    @Column(name = "offer_value")
    private double value;

    public Offer() {
    }

    public Offer(OfferProduct... offerProducts) {
        for(OfferProduct offerProduct : offerProducts) offerProduct.setOffer(this);
        this.offerProducts = Stream.of(offerProducts).collect(Collectors.toSet());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OfferProduct> getOfferProducts() {
        return new ArrayList<>(offerProducts);
    }

    public void setOfferProducts(List<OfferProduct> offerProducts) {
        if (offerProducts == null)
            this.offerProducts = null;
        else
            this.offerProducts = new HashSet<>(offerProducts);
    }
}
