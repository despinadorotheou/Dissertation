package com.dd186.admin.Services;

import com.dd186.admin.Domain.Category;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Domain.User;
import com.dd186.admin.Repositories.CategoryRepository;
import com.dd186.admin.Repositories.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProductServiceIntegrationTest {

    @Mock
    private ProductRepository mockProductRepository;
    @Mock
    private CategoryRepository mockCategoryRepository;

    private ProductService productServiceUnderTest;
    private Product product;
    private Category category;
    private List<Product> productList = new ArrayList<>();
    @Before
    public void setUp() {
        initMocks(this);
        productServiceUnderTest = new ProductService(mockProductRepository,mockCategoryRepository);
        category = new Category("Cold Drinks");
        product = new Product(1, "milk", "semi",1.00, 50,category );
        Product product1 =  new Product(2, "water", "550ml",0.50, 50,category );
        productList.add(product);
        productList.add(product1);
        Mockito.when(mockProductRepository.save(any()))
                .thenReturn(product);
        Mockito.when(mockProductRepository.findById(anyInt()))
                .thenReturn(product);
        Mockito.when(mockProductRepository.findAll())
                .thenReturn(productList);


    }

    @Test
    public void testFindProductById() {
        // Setup
        final int id = 1;

        // Run the test
        final Product result = productServiceUnderTest.findById(id);

        // Verify the results
        assertEquals(id, result.getId());
    }

    @Test
    public void testFindAllProducts() {

        // Run the test
        final List<Product> result = productServiceUnderTest.findAll();

        // Verify the results
        assertEquals(productList, result);
    }

    @Test
    public void testSaveProduct() {
        // Setup
        final int id = 1;

        // Run the test
        Product result = productServiceUnderTest.save(product);

        // Verify the results
        assertEquals(id, result.getId());
    }

//    @Test
//    public void testFindCategoryByName() {
//        // Setup
//        final String nameCategory = "Cold Drinks";
//
//        // Run the test
//        Category result = productServiceUnderTest.findByCategory(nameCategory);
//
//        // Verify the results
//        assertEquals(nameCategory, result.getCategory());
//    }
//
//    @Test
//    public void testDeleteProduct() {
//        // Setup
//        doAnswer((Answer<Void>) invocation -> {
//            Object[] arguments = invocation.getArguments();
//            if (arguments != null && arguments.length == 1 && arguments[0] != null ) {
//
//                Product product = (Product) arguments[0];
//
//
//            }
//            return null;
//        }).when(productServiceUnderTest).delete(any(Product.class));
//
//        // Run the test
//        final Product result = productServiceUnderTest.findById(id);
//
//        // Verify the results
//        assertEquals(id, result.getId());
//    }
}
