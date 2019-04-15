package com.dd186.admin.Domain.Offer;

import com.dd186.admin.Domain.Product;
import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;
import java.util.*;

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
    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfferProduct> offerProducts = new ArrayList<>() ;
    @Column(name = "offer_value")
    private double value;
    @Column(name = "offer_image")
    private Blob image;

    public Offer() {
    }

    public Offer(String description, double value) {
        this.description = description;
        this.value = value;
    }

    public void addProduct(Product product, int quantity) {
        OfferProduct offerProduct = new OfferProduct(this, product, quantity);
        offerProducts.add(offerProduct);
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
        return offerProducts;
    }

    public void setOfferProducts(List<OfferProduct> offerProducts) {
        this.offerProducts = offerProducts;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
