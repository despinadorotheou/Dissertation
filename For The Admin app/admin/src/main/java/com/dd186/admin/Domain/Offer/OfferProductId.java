package com.dd186.admin.Domain.Offer;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OfferProductId implements Serializable {

    @Column(name = "offer_id")
    private int offerId;

    @Column(name = "product_id")
    private int productId;

    public OfferProductId(int offerId, int productId) {
        this.offerId = offerId;
        this.productId = productId;
    }

    public OfferProductId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        OfferProductId that = (OfferProductId) o;
        return Objects.equals(offerId, that.offerId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerId, productId);
    }
}
