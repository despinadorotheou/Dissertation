package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Category;
import com.dd186.admin.Domain.Deal.Deal;
import com.dd186.admin.Services.DealService;
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
@WebMvcTest(DealsController.class)
public class DealControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private DealService dealService;

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testEditDeal()
            throws Exception {
        Deal deal = new Deal("deal 1", 1.00);
        deal.setId(1);

        given(dealService.findAll()).willReturn(new ArrayList<>(Collections.singleton(deal)));


        mvc.perform(get("/main/deals/edit")
                .param("dealId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("editDeal"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testAddNewDeal()
            throws Exception {

        File file = new File("src/test/java/com/dd186/admin/images/test_image.jpg");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        Category category = new Category("Drinks");


        given(productService.findByCategory(any(String.class))).willReturn(category);


        mvc.perform(multipart("/main/deals/add").file(multipartFile)
                .param("id", "-1")
                .param("category1", "1")
                .param("category2", "1")
                .param("description", "milk")
                .param("value", "1.00")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main/deals"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testAddDeal()
            throws Exception {
        Deal deal = new Deal("deal 1", 1.00);
        deal.setId(1);
        File file = new File("src/test/java/com/dd186/admin/images/test_image.jpg");
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        Category category = new Category("Drinks");



        given(productService.findByCategory(any(String.class))).willReturn(category);
        given(dealService.findById(anyInt())).willReturn(deal);


        mvc.perform(multipart("/main/deals/add").file(multipartFile)
                .param("id", "-1")
                .param("category1", "1")
                .param("category2", "1")
                .param("description", "milk")
                .param("value", "1.00")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main/deals"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testDealImage()
            throws Exception {

        File file = new File("src/test/java/com/dd186/admin/images/test_image.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        Deal deal = new Deal("deal 1", 1.00);
        deal.setId(1);
        deal.setImage(new javax.sql.rowset.serial.SerialBlob(multipartFile.getBytes()));

        given(dealService.findById(anyInt())).willReturn(deal);


        mvc.perform(get("/main/deals/image")
                .param("dealId", "1")
                .contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testDeleteOffer()
            throws Exception {
        Deal deal = new Deal("deal 1", 1.00);
        deal.setId(1);

        given(dealService.findById(anyInt())).willReturn(deal);

        mvc.perform(get("/main/deals/delete")
                .param("dealId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main/deals"));

    }
}
