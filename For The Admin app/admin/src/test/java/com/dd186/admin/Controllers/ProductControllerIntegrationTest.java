package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Category;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Services.ProductService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testEditProduct()
            throws Exception {
        Category category = new Category("Cold Drinks");

        Product product = new Product(1, "milk", "semi",1.00, 50,category );


        given(productService.findAll()).willReturn(new ArrayList<>(Collections.singleton(product)));


        mvc.perform(get("/main/products/edit")
                .param("productId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testAddNewProduct()
            throws Exception {

        Category category = new Category("Cold Drinks");
        File file = new File("src/test/java/com/dd186/admin/images/test_image.jpg");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        Product product = new Product();


        given(productService.findByCategory(any(String.class))).willReturn(category);
        given(productService.findById(anyInt())).willReturn(product);


        mvc.perform(multipart("/main/products/add").file(multipartFile)
                .param("id", "-1")
                .param("name", "Milk")
                .param("description", "4lt")
                .param("ingredients", "milk")
                .param("price", "1.00")
                .param("quantity", "100")
                .param("category", "Cold Drinks")
                .param("preference", "Vegetarian")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main/products"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testAddProduct()
            throws Exception {

        Category category = new Category("Cold Drinks");
        File file = new File("src/test/java/com/dd186/admin/images/test_image.jpg");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        Product product = new Product();
        product.setId(1);


        given(productService.findByCategory(any(String.class))).willReturn(category);
        given(productService.findById(anyInt())).willReturn(product);


        mvc.perform(multipart("/main/products/add").file(multipartFile)
                .param("id", "1")
                .param("name", "Milk")
                .param("description", "4lt")
                .param("ingredients", "milk")
                .param("price", "1.00")
                .param("quantity", "100")
                .param("category", "Cold Drinks")
                .param("preference", "Vegetarian")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main/products"));

    }
    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testProductImage()
            throws Exception {
        Category category = new Category("Cold Drinks");

        Product product = new Product(1, "milk", "semi",1.00, 50,category );
        File file = new File("src/test/java/com/dd186/admin/images/test_image.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        product.setImage(new javax.sql.rowset.serial.SerialBlob(multipartFile.getBytes()));

        given(productService.findById(anyInt())).willReturn(product);


        mvc.perform(get("/main/products/image")
                .param("productId", "1")
                .contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testDeleteProduct()
            throws Exception {
        Category category = new Category("Cold Drinks");

        Product product = new Product(1, "milk", "semi",1.00, 50,category );

        given(productService.findById(anyInt())).willReturn(product);

        mvc.perform(get("/main/products/delete")
                .param("productId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main/products"));

    }
}
