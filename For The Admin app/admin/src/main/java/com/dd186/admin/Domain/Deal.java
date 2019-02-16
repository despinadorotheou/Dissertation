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
@Table(name = "deal")
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deal_id", nullable = false)
    private int id;
    @Column(name = "deal_description", nullable = false)
    private String description;
    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL)
    private Set<DealCategory> dealCategories;
    @Column(name = "deal_value")
    private double value;

    public Deal() {
    }

    public Deal(DealCategory... dealCategories) {
        for(DealCategory dealCategory : dealCategories) dealCategory.setDeal(this);
        this.dealCategories = Stream.of(dealCategories).collect(Collectors.toSet());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DealCategory> getDealCategories() {
        return new ArrayList<>(dealCategories);
    }

    public void setDealCategories(List<DealCategory> dealCategories) {
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
