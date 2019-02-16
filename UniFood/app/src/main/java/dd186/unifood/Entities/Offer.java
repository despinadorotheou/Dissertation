package dd186.unifood.Entities;

import java.util.List;

public class Offer {

    private int id;
    private String description;
    private List<Product> productsInOffer;
    private double value;

    public Offer() {
    }

    public Offer(int id, String description, List<Product> productsInOffer, double value) {
        this.id = id;
        this.description = description;
        this.productsInOffer = productsInOffer;
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

    public List<Product> getProductsInOffer() {
        return productsInOffer;
    }

    public void setProductsInOffer(List<Product> productsInOffer) {
        this.productsInOffer = productsInOffer;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
