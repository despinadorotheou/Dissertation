package dd186.unifood.Entities;

import java.util.List;

public class Offer {

    private int id;
    private String description;
    private List<Product> productsInOffer;
    private double value;
    private String image;


    public Offer() {
    }

    public Offer(int id, String description, double value, List<Product> offerProducts) {
        this.id = id;
        this.description = description;
        this.productsInOffer = offerProducts;
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

    public void setProductsInOffer(List<Product> offerProducts) {
        this.productsInOffer = offerProducts;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
