package com.dd186.admin.Domain.Deal;

import com.dd186.admin.Domain.Offer.OfferProductId;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DealCategoryId implements Serializable{

    @Column(name = "deal_id")
    private int dealId;

    @Column(name = "category_id")
    private int categoryId;

    public DealCategoryId(int dealId, int categoryId) {
        this.dealId = dealId;
        this.categoryId = categoryId;
    }

    public DealCategoryId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        DealCategoryId that = (DealCategoryId) o;
        return Objects.equals(dealId, that.dealId) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dealId, categoryId);
    }
}
