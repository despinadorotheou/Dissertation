package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Product;
import com.dd186.admin.Domain.User;
import com.dd186.admin.Services.ProductService;
import com.dd186.admin.Services.UserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class Rest {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    //http://10.0.2.2:8080/rest/products
    @RequestMapping(value = "/products")
    public String sendProducts() throws SQLException, IOException {
        List<Product> products = productService.findAll();
        JsonArray result = new JsonArray();
        for (Product p:products) {
            JsonObject product = new JsonObject();
            product.addProperty("id",p.getId());
            product.addProperty("name",p.getName());
            product.addProperty("description",p.getDescription());
            product.addProperty("price",p.getPrice());
            product.addProperty("quantity",p.getQuantity());
            product.addProperty("image", Base64.encodeBase64String(p.getImage().getBytes(1, (int) p.getImage().length())));
            JsonObject category = new JsonObject();
            category.addProperty("id",p.getCategory().getId());
            category.addProperty("category",p.getCategory().getCategory());
            product.add("category",category);
            result.add(product);
        }
        return result.toString();

    }

    //http://10.0.2.2:8080/rest/login/"+email.getText().toString()+"/" +pass.getText().toString()
    @RequestMapping(value = "/login/{email}/{pass}")
    public String validateLogin(@PathVariable("email") String userEmail, @PathVariable("pass") String userPass){
        User user = userService.findUserByEmail(userEmail);
        if (user!=null){
            if (userService.passMatch(userPass, user.getPassword())){
                JsonObject product = new JsonObject();
                product.addProperty("id",user.getId());
                product.addProperty("name",user.getName());
                product.addProperty("lastName",user.getLastName());
                product.addProperty("email",user.getEmail());
                product.addProperty("password",user.getPassword());
                return product.toString();
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
            userService.saveUser(newuser);
            return "ok";
        }
        return "Already existing user";
    }



}
