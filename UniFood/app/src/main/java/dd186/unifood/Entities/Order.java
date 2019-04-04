package dd186.unifood.Entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class Order implements Serializable {

    private int id;
    private String date;
    private double value;
    private HashMap<String, Integer> products;
    private boolean paid;


    public Order() {
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setProducts(HashMap<String, Integer> products) {
        this.products = products;
    }

    public HashMap<String, Integer> getProducts() {
        return products;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
