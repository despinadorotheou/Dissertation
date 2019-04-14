package com.dd186.admin.Domain.Deal;

import com.dd186.admin.Domain.Category;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "DealCategory")
@Table(name = "deal_category")
public class DealCategory implements Serializable {

    @EmbeddedId
    private DealCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dealId")
    private Deal deal;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    private Category category;

    @Column(name = "quantity")
    private int quantity;

    public DealCategory(Deal deal, Category category, int quantity) {
        this.deal = deal;
        this.category = category;
        this.quantity = quantity;
        this.id = new DealCategoryId(deal.getId(),category.getId());
    }

    public DealCategory() {
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
