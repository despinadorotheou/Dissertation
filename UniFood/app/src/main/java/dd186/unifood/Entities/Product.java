package dd186.unifood.Entities;

import java.io.Serializable;

public class Product implements Serializable {

    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String image;
    private String preference;
    private Category category;
    private int quantityInBasket;
    private int quantityInOrder;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public int getQuantityInOrder() {
        return quantityInOrder;
    }

    public void setQuantityInOrder(int quantityInOrder) {
        this.quantityInOrder = quantityInOrder;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public Product() {
    }

    public Product(int id, String name, String description, double price, int quantity, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}

