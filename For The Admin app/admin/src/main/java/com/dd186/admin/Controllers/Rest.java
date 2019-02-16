package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.*;
import com.dd186.admin.Services.DealService;
import com.dd186.admin.Services.OfferService;
import com.dd186.admin.Services.ProductService;
import com.dd186.admin.Services.UserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    //http://10.0.2.2:8080/rest/products
    @RequestMapping(value = "/products")
    public String sendProducts() throws SQLException, IOException {
        List<Product> products = productService.findAll();
        JsonArray toRet = createProductList(products);
        if (toRet.isJsonNull())
            return "";
        else
            return createProductList(products).toString();
    }

    //http://10.0.2.2:8080/rest/deals
    @RequestMapping(value = "/deals")
    public String sendDeals() {
        List<Deal> deals = dealService.findAll();
        JsonArray toRet = new JsonArray();
        if (!deals.isEmpty()){
            for (Deal d:deals) {
                JsonObject deal = new JsonObject();
                deal.addProperty("id", d.getId());
                deal.addProperty("description", d.getDescription());
                deal.addProperty("value", d.getValue());
                JsonArray categoriesInDeal = new JsonArray();
//                for (Category c: d.getDealCategories()) {
//                    JsonObject category = new JsonObject();
//                    category.addProperty("id", c.getId());
//                    category.addProperty("category", c.getCategory());
//                    categoriesInDeal.add(category);
//                }
                deal.addProperty("dealCategories",categoriesInDeal.toString());
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
//                offer.addProperty("offerProducts",createProductList(o.getOfferProducts()).toString());
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
                userProperties.add("favouriteProducts", createProductList(favourites));
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


    //method for creating Json Array for the android app
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
                JsonObject category = new JsonObject();
                category.addProperty("id", p.getCategory().getId());
                category.addProperty("category", p.getCategory().getCategory());
                product.add("category", category);
                result.add(product);
            }
        }
        return result;
    }




}
