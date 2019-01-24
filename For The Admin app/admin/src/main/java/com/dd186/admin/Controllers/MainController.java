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
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
            modelAndView.addObject("categories", productService.findAllCategories());
            modelAndView.addObject("product", product);
        }
        modelAndView.setViewName("editProduct");
        return modelAndView;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView addChange(@RequestParam(name ="id", required = false,  defaultValue="-1") int id,
                                  @RequestParam(name ="name") String name,
                                  @RequestParam(name ="description", required = false) String description,
                                  @RequestParam(name ="price") double price,
                                  @RequestParam(name = "quantity") int quantity,
                                  @RequestParam(name = "category") String cat){
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
        productService.save(product);
        modelAndView.addObject("products",(List<Product>)productService.findAll());
        modelAndView.setViewName("main");
        return modelAndView;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView productMaster(@RequestParam(value="productId", required=false, defaultValue="-1") int productId) {
        ModelAndView modelAndView = new ModelAndView();
        Product p = productService.findById(productId);
        if (p != null)
            productService.delete(p);
        modelAndView.addObject("products",(List<Product>)productService.findAll());
        modelAndView.setViewName("main");
        return modelAndView;
    }

}
