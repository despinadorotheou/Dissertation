package com.dd186.admin.Domain.Deal;

import com.dd186.admin.Domain.Category;
import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Table(name = "deal")
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deal_id", nullable = false)
    private int id;
    @Column(name = "deal_description", nullable = false)
    private String description;
    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DealCategory> dealCategories = new ArrayList<>();
    @Column(name = "deal_value")
    private double value;
    @Column(name = "deal_image")
    private Blob image;

    public Deal() {
    }

    public Deal(String description, double value) {
        this.description = description;
        this.value = value;
    }

    public void addCategory(Category category, int quantity) {
        DealCategory dealCategory = new DealCategory(this, category, quantity);
        dealCategories.add(dealCategory);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DealCategory> getDealCategories() {
        return dealCategories;
    }

    public void setDealCategories(List<DealCategory> dealCategories) {
        this.dealCategories = dealCategories;
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

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dealCategories=" + dealCategories +
                ", value=" + value +
                '}';
    }
}
