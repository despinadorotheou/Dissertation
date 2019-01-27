package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Category;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Repositories.ProductRepository;
import com.dd186.admin.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.mock.web.MockMultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView editProduct(@RequestParam(value="productId", required=false, defaultValue="-1") int productId) {
        ModelAndView modelAndView = new ModelAndView();
        Product product = new Product();
        if(productId>0){
            Product product2 = ((List<Product>) productService.findAll()).stream().filter(p -> (((Product) p).getId() == productId)).findAny().get();
            product.setId(productId);
            product.setDescription(product2.getDescription());
            product.setName(product2.getName());
            product.setPrice(product2.getPrice());
            product.setQuantity(product2.getQuantity());
            product.setCategory(product2.getCategory());
            product.setImage(product2.getImage());
            modelAndView.addObject("product", product);
        }
        modelAndView.addObject("categories", productService.findAllCategories());
        modelAndView.setViewName("editProduct");
        return modelAndView;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView addChange(@RequestParam(name ="id", required = false,  defaultValue="-1") int id,
                                  @RequestParam(name ="name") String name,
                                  @RequestParam(name ="description", required = false) String description,
                                  @RequestParam(name ="price") double price,
                                  @RequestParam(name = "quantity") int quantity,
                                  @RequestParam(name = "category") String cat,
                                  @RequestParam(name = "image", required = false)MultipartFile image) throws IOException, SQLException {
        ModelAndView modelAndView = new ModelAndView();
        Product product = new Product();
        if (id != -1){
            product.setId(id);
        }
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategory(productService.findByCategory(cat));
        if (!image.isEmpty()){
            Blob blob = new javax.sql.rowset.serial.SerialBlob(image.getBytes());
            product.setImage(blob);
        } else if(id!=-1){
            Product lastProduct = productService.findById(id);
            product.setImage(lastProduct.getImage());
        }
        productService.save(product);
        modelAndView.addObject("products",(List<Product>)productService.findAll());
        modelAndView.setViewName("main");
        return modelAndView;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView productMaster(@RequestParam(value="productId", required=false, defaultValue="-1") int productId) {
        ModelAndView modelAndView = new ModelAndView();
        Product p = productService.findById(productId);
        if (p != null) {
            p.setCategory(null);
            productService.delete(p);
        }
        modelAndView.addObject("products",(List<Product>)productService.findAll());
        modelAndView.setViewName("main");
        return modelAndView;
    }

}
