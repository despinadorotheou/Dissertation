package com.dd186.admin.Domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class DealCategory implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn
    private Deal deal;
    @Id
    @ManyToOne
    @JoinColumn
    private Category category;

    private int quantity;

    public DealCategory(Category category, int quantity) {
        this.category = category;
        this.quantity = quantity;
    }

    public DealCategory() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(deal.getId(), category.getId(), quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DealCategory)) return false;
        DealCategory that = (DealCategory) obj;
        return Objects.equals(deal.getId(), that.deal.getId()) &&
                Objects.equals(category.getId(), that.category.getId()) &&
                Objects.equals(quantity, that.quantity);
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
