package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Product;
import com.dd186.admin.Domain.User;
import com.dd186.admin.Repositories.ProductRepository;
import com.dd186.admin.Repositories.UserRepository;
import com.dd186.admin.Services.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class Rest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @GetMapping(value = "/products")
    public String sendProducts(){
        Gson gsonBuilder = new GsonBuilder().create();
        List<Product> products = productRepository.findAll();
        String jsonForProducts = gsonBuilder.toJson(products);
        return jsonForProducts;
    }
    //http://10.0.2.2:8080/rest/login/"+email.getText().toString()+"/" +pass.getText().toString()
    @GetMapping(value = "/login/{email}/{pass}")
    public String validateLogin(@PathVariable("email") String userEmail, @PathVariable("pass") String userPass){
        User user = userRepository.findByEmail(userEmail);
        if (user!=null){
            if (userService.passMatch(userPass, user.getPassword())){
                return "ok";
            } else return "invalid password";
        } else return "invalid username";
    }

    //httpRequest.setLink("http://10.0.2.2:8080/rest/signup/"+useremail+"/" +userName+"/" +last+"/" +userdob+"/" +password1);
    @GetMapping(value = "/signup/{email}/{name}/{lastname}/{password}")
    public String validateSignup(@PathVariable("email") String userEmail,
                                 @PathVariable("name") String userName,
                                 @PathVariable("lastname") String userlast,
                                 @PathVariable("password") String pass){
        User user = userRepository.findByEmail(userEmail);
        if (user==null){
            User newuser = new User(userName,userlast,userEmail,pass);
            userService.saveUser(newuser);
            return "ok";
        }
        return "Already existing user";
    }



}
