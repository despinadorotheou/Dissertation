package com.dd186.admin.Domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;


@Entity
public class OfferProduct implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn
    private Offer offer;
    @Id
    @ManyToOne
    @JoinColumn
    private Product product;

    private int quantity;

    public OfferProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OfferProduct() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(offer.getId(), product.getId(), quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OfferProduct)) return false;
        OfferProduct that = (OfferProduct) obj;
        return Objects.equals(offer.getId(), that.offer.getId()) &&
                Objects.equals(product.getId(), that.product.getId()) &&
                Objects.equals(quantity, that.quantity);
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
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
}
