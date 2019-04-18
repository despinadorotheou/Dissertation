package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Deal.Deal;
import com.dd186.admin.Domain.Offer.Offer;
import com.dd186.admin.Domain.Order.Order;
import com.dd186.admin.Domain.Order.OrderStatus;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Domain.User;
import com.dd186.admin.Services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OfferService offerService;

    @MockBean
    private DealService dealService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private AndroidPushNotificationsService androidPushNotificationsService;

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testOrderReady()
            throws Exception {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.PENDING);
        order.setUserid(6);

        given(orderService.findById(anyInt())).willReturn(order);
        given(androidPushNotificationsService.send(any())).willReturn(CompletableFuture.completedFuture("ok"));


        mvc.perform(get("/main/order/ready")
                .param("orderId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testOrderCollected()
            throws Exception {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.READY);
        order.setUserid(1);

        given(orderService.findById(anyInt())).willReturn(order);
        given(androidPushNotificationsService.send(any())).willReturn(CompletableFuture.completedFuture("ok"));


        mvc.perform(get("/main/order/collected")
                .param("orderId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/main"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testDirectDealsPage()
            throws Exception {
        Deal deal = new Deal("Deal 1", 1.00);

        given(dealService.findAll()).willReturn(new ArrayList<>(Collections.singleton(deal)));


        mvc.perform(get("/main/deals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("deals", new ArrayList<>(Collections.singleton(deal))))
                .andExpect(view().name("dealsPage"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testDirectOffersPage()
            throws Exception {
        Offer offer = new Offer("Offer 1", 1.00);

        given(offerService.findAll()).willReturn(new ArrayList<>(Collections.singleton(offer)));


        mvc.perform(get("/main/offers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("offers", new ArrayList<>(Collections.singleton(offer))))
                .andExpect(view().name("offersPage"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testDirectProductsPage()
            throws Exception {
        Product product = new Product();
        product.setName("Milk");

        given(productService.findAll()).willReturn(new ArrayList<>(Collections.singleton(product)));


        mvc.perform(get("/main/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", new ArrayList<>(Collections.singleton(product))))
                .andExpect(view().name("productsPage"));

    }
}
