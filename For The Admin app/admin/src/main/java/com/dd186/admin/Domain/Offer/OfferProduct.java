package com.dd186.admin.Domain.Offer;

import com.dd186.admin.Domain.Product;

import javax.persistence.*;
import java.io.Serializable;


@Entity(name = "OfferProduct")
@Table(name = "offer_product")
public class OfferProduct implements Serializable {

    @EmbeddedId
    private OfferProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("offerId")
    private Offer offer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    public OfferProduct(Offer offer, Product product, int quantity) {
        this.offer = offer;
        this.product = product;
        this.quantity = quantity;
        this.id = new OfferProductId(offer.getId(), product.getId());
    }

    public OfferProduct() {
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
