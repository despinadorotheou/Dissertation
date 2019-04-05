package dd186.unifood.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dd186.unifood.Main;

public class Offer {

    private int id;
    private String description;
    private HashMap<String, Integer> productsInOffer;
    private double value;
    private String image;
    private int quantityInBasket ;


    public Offer() {
    }

    public Offer(int id, String description, double value, HashMap<String, Integer> offerProducts) {
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
        List<Product> favProducts = new ArrayList<>();
        for (String s:productsInOffer.keySet()) {
            for (int i = 0; i< productsInOffer.get(s); i++) {
                for (Product p : Main.products) {
                    if (Integer.parseInt(s) == p.getId()) {
                        favProducts.add(p);
                        break;
                    }
                }
            }
        }
        return favProducts;
    }

    public void setProductsInOffer(HashMap<String, Integer> productsInOffer) {
        this.productsInOffer = productsInOffer;
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

    public int getQuantityInBasket() {
        return quantityInBasket;
    }

    public void setQuantityInBasket(int quantityInBasket) {
        this.quantityInBasket = quantityInBasket;
    }
}
