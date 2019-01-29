package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Product;
import com.dd186.admin.Domain.User;
import com.dd186.admin.Repositories.ProductRepository;
import com.dd186.admin.Repositories.UserRepository;
import com.dd186.admin.Services.ProductService;
import com.dd186.admin.Services.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

//import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.binary.Base64;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class Rest {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @GetMapping(value = "/products")
        public String sendProducts() throws SQLException, IOException {
//        Gson gsonBuilder = new GsonBuilder().create();
        List<Product> products = productService.findAll();
//        String jsonForProducts = gsonBuilder.toJson(products);
//        return jsonForProducts;
        JsonArray result = new JsonArray();
        for (Product p:products) {
            JsonObject product = new JsonObject();
            product.addProperty("id",p.getId());
            product.addProperty("name",p.getName());
            product.addProperty("description",p.getDescription());
            product.addProperty("price",p.getPrice());
            product.addProperty("quantity",p.getQuantity());
//            product.addProperty("image", new String(IOUtils.toByteArray(p.getImage().getBinaryStream())));
            product.addProperty("image", Base64.encodeBase64String(p.getImage().getBytes(1, (int) p.getImage().length())));

//            product.addProperty("image", new String(p.getImage().getBytes(1,(int) p.getImage().length())));
            JsonObject category = new JsonObject();
            category.addProperty("id",p.getCategory().getId());
            category.addProperty("category",p.getCategory().getCategory());
            product.add("category",category);
            result.add(product);
        }
        return result.toString();

    }
    //method for testing proposes
//    @RequestMapping(value = "/image", method = RequestMethod.GET)
//    public void getImageAsByteArray(HttpServletResponse response) throws IOException, SQLException {
//        InputStream in = productService.findAll().get(1).getImage().getBinaryStream();
//        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//        IOUtils.copy(in, response.getOutputStream());
//    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public @ResponseBody byte[] getImageAsByteArray() throws IOException, SQLException {
        InputStream in = productService.findAll().get(1).getImage().getBinaryStream();
        return IOUtils.toByteArray(in);
    }

    //http://10.0.2.2:8080/rest/login/"+email.getText().toString()+"/" +pass.getText().toString()
    @GetMapping(value = "/login/{email}/{pass}")
    public String validateLogin(@PathVariable("email") String userEmail, @PathVariable("pass") String userPass){
        User user = userService.findUserByEmail(userEmail);
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
        User user = userService.findUserByEmail(userEmail);
        if (user==null){
            User newuser = new User(userName,userlast,userEmail,pass);
            userService.saveUser(newuser);
            return "ok";
        }
        return "Already existing user";
    }



}
