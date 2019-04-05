package dd186.unifood.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dd186.unifood.Main;

public class User {
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private List<Integer> favouriteProducts;
    private List<Order> orders;

    public User(int id, String name, String lastName, String email, String password, List<Integer> favouriteProducts) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.favouriteProducts = favouriteProducts;
    }

    public User() {
    }

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Product> getFavouriteProducts() {
        List<Product> products = Main.products;
        List<Product> toRet =  new ArrayList<>();
        for (Integer i:favouriteProducts) {
            for (Product p : products) {
                if (p.getId() == i){
                    toRet.add(p);
                    break;
                }
            }
        }
        return  toRet;
    }

    public void setFavouriteProducts(List<Integer> favouriteProducts) {
        this.favouriteProducts = favouriteProducts;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
