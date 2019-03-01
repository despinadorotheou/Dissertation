package dd186.unifood.Entities;

import java.io.Serializable;

public class Category implements Serializable {

    private int id;
    private String category;
    private int quantityInDeal;

    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Category() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantityInDeal() {
        return quantityInDeal;
    }

    public void setQuantityInDeal(int quantityInDeal) {
        this.quantityInDeal = quantityInDeal;
    }
}
