package com.dd186.admin.Domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "deal_category", joinColumns = @JoinColumn(name = "deal_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> dealCategories;
    @Column(name = "deal_value")
    private double value;

    public Deal() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Category> getDealCategories() {
        return new ArrayList<>(dealCategories);
    }

    public void setDealCategories(List<Category> dealCategories) {
        if (dealCategories == null)
            this.dealCategories = null;
        else
            this.dealCategories = new HashSet<>(dealCategories);
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
