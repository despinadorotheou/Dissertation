package dd186.unifood.Entities;

import java.util.List;

public class Deal {

    private int id;
    private String description;
    private List<Category> categoriesInDeal;
    private double value;

    public Deal() {
    }

    public Deal(int id, String description, List<Category> categoriesInDeal, double value) {
        this.id = id;
        this.description = description;
        this.categoriesInDeal = categoriesInDeal;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Category> getCategoriesInDeal() {
        return categoriesInDeal;
    }

    public void setCategoriesInDeal(List<Category> categoriesInDeal) {
        this.categoriesInDeal = categoriesInDeal;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
