package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.*;
import com.dd186.admin.Services.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/rest")
public class Rest {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    DealService dealService;
    @Autowired
    OfferService offerService;
    @Autowired
    OrderService orderService;

    //http://10.0.2.2:8080/rest/products
    @RequestMapping(value = "/products")
    public String sendProducts() throws SQLException, IOException {
        List<Product> products = productService.findAll();
        JsonArray toRet = createProductList(products);
        if (toRet.isJsonNull())
            return "";
        else
            return toRet.toString();
    }

    //http://10.0.2.2:8080/rest/deals
    @RequestMapping(value = "/deals")
    public String sendDeals() throws SQLException {
        List<Deal> deals = dealService.findAll();
        JsonArray toRet = new JsonArray();
        if (!deals.isEmpty()){
            for (Deal d:deals) {
                JsonObject deal = new JsonObject();
                deal.addProperty("id", d.getId());
                deal.addProperty("description", d.getDescription());
                deal.addProperty("value", d.getValue());
                JsonArray categoriesInDeal = new JsonArray();
                for (DealCategory dealCategory : d.getDealCategories()) {
                    JsonObject category = new JsonObject();
                    category.addProperty("id", dealCategory.getCategory().getId());
                    category.addProperty("category", dealCategory.getCategory().getCategory());
                    category.addProperty("quantityInDeal", dealCategory.getQuantity());
                    categoriesInDeal.add(category);
                }

                deal.addProperty("image",Base64.encodeBase64String(d.getImage().getBytes(1, (int) d.getImage().length())) );
                deal.add("categoriesInDeal",categoriesInDeal);
                toRet.add(deal);
            }
            return toRet.toString();
        } else {
            return "";
        }
    }

    //http://10.0.2.2:8080/rest/offers
    @RequestMapping(value = "/offers")
    public String sendOffers() throws SQLException {
        List<Offer> offers = offerService.findAll();
        JsonArray toRet = new JsonArray();
        if (!offers.isEmpty()){
            for (Offer o:offers) {
                JsonObject offer = new JsonObject();
                offer.addProperty("id", o.getId());
                offer.addProperty("description", o.getDescription());
                offer.addProperty("value", o.getValue());
                offer.addProperty("image",Base64.encodeBase64String(o.getImage().getBytes(1, (int) o.getImage().length())) );
                offer.add("productsInOffer", mapFromOfferProductList(o.getOfferProducts()));
                toRet.add(offer);
            }
            return toRet.toString();
        } else {
            return "";
        }
    }

    //http://10.0.2.2:8080/rest/login/"+email.getText().toString()+"/" +pass.getText().toString()
    @RequestMapping(value = "/login/{email}/{pass}")
    public String validateLogin(@PathVariable("email") String userEmail, @PathVariable("pass") String userPass) throws SQLException {
        User user = userService.findUserByEmail(userEmail);
        if (user!=null){
            if (userService.passMatch(userPass, user.getPassword())){
                JsonObject userProperties = new JsonObject();
                userProperties.addProperty("id",user.getId());
                userProperties.addProperty("name",user.getName());
                userProperties.addProperty("lastName",user.getLastName());
                userProperties.addProperty("email",user.getEmail());
                userProperties.addProperty("password",user.getPassword());
                List<Product> favourites = new ArrayList<>();
                favourites.addAll(user.getFavProduct());
                userProperties.add("favouriteProducts", toIntegerList(favourites));
                userProperties.add("orders", createOfferList(new ArrayList<>(user.getOrders())));
                return userProperties.toString();
            } else return "invalid";
        } else return "invalid";
    }

    //http://10.0.2.2:8080/rest/signup/"+useremail+"/" +userName+"/" +last+"/" +userdob+"/" +password1
    @RequestMapping(value = "/signup/{email}/{name}/{lastname}/{password}")
    public String validateSignup(@PathVariable("email") String userEmail,
                                 @PathVariable("name") String userName,
                                 @PathVariable("lastname") String userlast,
                                 @PathVariable("password") String pass){
        User user = userService.findUserByEmail(userEmail);
        if (user==null){
            User newuser = new User(userName,userlast,userEmail,pass);
            userService.saveUserCustom(newuser);
            return "ok";
        }
        return "Already existing user";
    }

    //http://10.0.2.2:8080/rest/addFavourite/"+userID+"/" +productID
    @RequestMapping(value = "/addFavourite/{userID}/{productID}")
    public void addFavourite(@PathVariable("userID") int user_id, @PathVariable("productID") int product_id){
        User user = userService.findById(user_id);
        Product product =  productService.findById(product_id);
        List<Product> favourites = new ArrayList<>(user.getFavProduct());
        favourites.add(product);
        user.setFavProduct(new HashSet<>(favourites));
        userService.saveUser(user);

    }

    //http://10.0.2.2:8080/rest/removeFavourite/"+userID+"/" +productID
    @RequestMapping(value = "/removeFavourite/{userID}/{productID}")
    public void removeFavourite(@PathVariable("userID") int user_id, @PathVariable("productID") int product_id){
        User user = userService.findById(user_id);
        Product product =  productService.findById(product_id);
        List<Product> favourites = new ArrayList<>(user.getFavProduct());
        favourites.remove(product);
        user.setFavProduct(new HashSet<>(favourites));
        userService.saveUser(user);
    }

    //http://10.0.2.2:8080/rest/sendOrder/"+userID
    @RequestMapping(value = "/addOrder/{userID}/{price}")
    public String addOrder(@PathVariable("userID") int user_id,@PathVariable("price") double price, @RequestBody HashMap<String,String> map) throws IOException {
        User user = userService.findById(user_id);
        List<OrderProduct> inOrder = new ArrayList<>();
        for (String i :map.keySet()) {
            Product product = productService.findById(Integer.parseInt(i));
            int quantity = Integer.parseInt(map.get(i));
            product.setQuantity(product.getQuantity()-quantity);
            productService.save(product);
            OrderProduct orderProduct = new OrderProduct(product, quantity);
            inOrder.add(orderProduct);
        }
        Order order = new Order(inOrder,price);
        orderService.save(order);
        user.getOrders().add(order);
        userService.saveUser(user);
        return "ok";
    }




    //method for creating Json Array of the products for the android app
    private JsonArray createProductList(List<Product> products) throws SQLException {
        JsonArray result = new JsonArray();
        if (!products.isEmpty()) {
            for (Product p : products) {
                JsonObject product = new JsonObject();
                product.addProperty("id", p.getId());
                product.addProperty("name", p.getName());
                product.addProperty("description", p.getDescription());
                product.addProperty("price", p.getPrice());
                product.addProperty("quantity", p.getQuantity());
                product.addProperty("image", Base64.encodeBase64String(p.getImage().getBytes(1, (int) p.getImage().length())));
                product.addProperty("preference", p.getPreference());
                product.addProperty("ingredients", p.getIngredients());
                JsonObject category = new JsonObject();
                category.addProperty("id", p.getCategory().getId());
                category.addProperty("category", p.getCategory().getCategory());
                product.add("category", category);
                result.add(product);
            }
        }
        return result;
    }

    //method used to create a map of products based on the offerProduct list
    private JsonObject mapFromOfferProductList(List<OfferProduct> offerProducts){
        HashMap<Integer, Integer> products = new HashMap<>();
        for (OfferProduct op:offerProducts) {
            products.put(op.getProduct().getId(), op.getQuantity());
        }
        JsonObject map = new JsonObject();
        for (Integer i:products.keySet()) {
            map.addProperty(String.valueOf(i), products.get(i));
        }

        return map;
    }

    //method used to create an objectarray from a list of orders
    private JsonArray createOfferList(List<Order> orders) throws SQLException {
        JsonArray result = new JsonArray();
        if (!orders.isEmpty()) {
            for (Order o : orders) {
                JsonObject order = new JsonObject();
                order.addProperty("id", o.getId());
                Date date=new Date(o.getDate().getTime());
                order.addProperty("date", String.valueOf(date));
                order.addProperty("value", o.getValue());
                order.add("products", mapFromOrderProductList(o.getOrderProducts()));
                result.add(order);
            }
        }
        return result;
    }

    //method used to create a hashmap of products based on the orderproduct list
    private JsonObject mapFromOrderProductList(List<OrderProduct> orderProducts){
        HashMap<Integer, Integer> products = new HashMap<>();
        for (OrderProduct op:orderProducts) {
            products.put(op.getProduct().getId(), op.getQuantity());
        }

        JsonObject map = new JsonObject();
        for (Integer i:products.keySet()) {
            map.addProperty(String.valueOf(i), products.get(i));
        }

        return map;
    }

    //method used to create an integer list json array with ids from a list of products
    private JsonArray toIntegerList(List<Product> products){
        List<Integer> list = new ArrayList<>();
        for (Product p:products) {
            list.add(p.getId());
        }
        JsonArray arr = new JsonArray();
        for(int i : list) {
            arr.add(i);
        }
        return arr;

    }





}
