package com.dd186.admin.Services;

import com.dd186.admin.Domain.Category;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Repositories.CategoryRepository;
import com.dd186.admin.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Product save(Product product) {
        productRepository.save(product);
        return product;
    }

    public void delete(Product product){
        productRepository.delete(product);
    }

    public Product findById(int id){
        return productRepository.findById(id);
    }

    public Category findByCategory(String category){
        return categoryRepository.findByCategory(category);
    }

    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }
}
