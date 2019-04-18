package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Category;
import com.dd186.admin.Domain.Offer.Offer;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Services.OfferService;
import com.dd186.admin.Services.ProductService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(OffersController.class)
public class OfferControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private OfferService offerService;

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testEditOffer()
            throws Exception {
        Offer offer = new Offer("offer 1", 1.00);
        offer.setId(1);

        given(offerService.findAll()).willReturn(new ArrayList<>(Collections.singleton(offer)));


        mvc.perform(get("/main/offers/edit")
                .param("offerId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("editOffer"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testAddNewProduct()
            throws Exception {

        File file = new File("src/test/java/com/dd186/admin/images/test_image.jpg");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        Product product = new Product();
        product.setId(1);


        given(productService.findById(anyInt())).willReturn(product);


        mvc.perform(multipart("/main/offers/add").file(multipartFile)
                .param("id", "-1")
                .param("product1", "1")
                .param("product2", "1")
                .param("description", "milk")
                .param("value", "1.00")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main/offers"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testAddProduct()
            throws Exception {
        Offer offer = new Offer("offer 1", 1.00);
        offer.setId(1);
        File file = new File("src/test/java/com/dd186/admin/images/test_image.jpg");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        Product product = new Product();
        product.setId(1);


        given(productService.findById(anyInt())).willReturn(product);
        given(offerService.findById(anyInt())).willReturn(offer);


        mvc.perform(multipart("/main/offers/add").file(multipartFile)
                .param("id", "1")
                .param("product1", "1")
                .param("product2", "1")
                .param("description", "milk")
                .param("value", "1.00")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main/offers"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testOfferImage()
            throws Exception {

        File file = new File("src/test/java/com/dd186/admin/images/test_image.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        Offer offer = new Offer("offer 1", 1.00);
        offer.setId(1);
        offer.setImage(new javax.sql.rowset.serial.SerialBlob(multipartFile.getBytes()));

        given(offerService.findById(anyInt())).willReturn(offer);


        mvc.perform(get("/main/offers/image")
                .param("offerId", "1")
                .contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testDeleteOffer()
            throws Exception {
        Offer offer = new Offer("offer 1", 1.00);
        offer.setId(1);

        given(offerService.findById(anyInt())).willReturn(offer);

        mvc.perform(get("/main/offers/delete")
                .param("offerId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main/offers"));

    }
}
