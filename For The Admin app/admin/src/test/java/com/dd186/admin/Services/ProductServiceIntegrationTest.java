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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    private Category category2;
    private List<Product> productList = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();

    @Before
    public void setUp() {
        initMocks(this);
        productServiceUnderTest = new ProductService(mockProductRepository,mockCategoryRepository);
        category = new Category("Cold Drinks");
        category2 = new Category("Cookies");
        categories.add(category);
        categories.add(category2);
        product = new Product(1, "milk", "semi",1.00, 50,category );
        Product product1 =  new Product(2, "water", "550ml",0.50, 50,category );
        productList.add(product);
        productList.add(product1);

    }

    @Test
    public void testFindProductById() {
        // Setup
        Mockito.when(mockProductRepository.findById(anyInt()))
                .thenReturn(product);
        final int id = 1;

        // Run the test
        final Product result = productServiceUnderTest.findById(id);

        // Verify the results
        assertEquals(id, result.getId());
    }

    @Test
    public void testFindAllProducts() {
        // Setup
        Mockito.when(mockProductRepository.findAll())
                .thenReturn(productList);

        // Run the test
        final List<Product> result = productServiceUnderTest.findAll();

        // Verify the results
        assertEquals(productList, result);
    }

    @Test
    public void testSaveProduct() {
        // Setup
        Mockito.when(productServiceUnderTest.findById(anyInt()))
                .thenReturn(null).thenReturn(product);
        final int id = 1;

        // Run the test
        Product existing = productServiceUnderTest.findById(id);
        productServiceUnderTest.save(product);
        Product result = productServiceUnderTest.findById(id);

        // Verify the results
        assertNotNull(result);
        assertEquals(result, product);
        assertNull(existing);
    }

    @Test
    public void testFindCategoryByName() {

        // Setup
        Mockito.when(mockCategoryRepository.findByCategory(any(String.class)))
                .thenReturn(category);
        final String nameCategory = "Cold Drinks";

        // Run the test
        Category result = productServiceUnderTest.findByCategory(nameCategory);

        // Verify the results
        assertEquals(nameCategory, result.getCategory());
    }

    @Test
    public void testDeleteProduct() {
        // Setup
        Mockito.when(productServiceUnderTest.findById(anyInt()))
                .thenReturn(product).thenReturn(null);

        // Run the test
        final Product beforeDelete = productServiceUnderTest.findById(1);
        productServiceUnderTest.delete(beforeDelete);
        final Product result = productServiceUnderTest.findById(1);


        // Verify the results
        assertNotNull(beforeDelete);
        assertEquals(beforeDelete, product);
        assertNull(result);
    }

    @Test
    public void testFindAllCategories() {
        // Setup
        Mockito.when(mockCategoryRepository.findAll())
                .thenReturn(categories);

        // Run the test
        final List<Category> result = productServiceUnderTest.findAllCategories();

        // Verify the results
        assertEquals(categories, result);
    }
}
