package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Category;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@WebMvcTest(Rest.class)
public class RestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OfferService offerService;

    @MockBean
    private DealService dealService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @Test
    public void givenProducts_whenSendProducts_thenReturnJsonArray()
            throws Exception {
        Category category = new Category("Cold Drinks");
        Product product = new Product(1, "milk", "semi",1.00, 50,category );

        List<Product> products = Collections.singletonList(product);

        given(productService.findAll()).willReturn(products);

        mvc.perform(get("/rest/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(product.getName())));
    }

    @Test
    public void givenDeals_whenSendDeals_thenReturnJsonArray()
            throws Exception {
        Deal deal = new Deal ( "Deal 1", 2.0);

        List<Deal> deals = Collections.singletonList(deal);

        given(dealService.findAll()).willReturn(deals);

        mvc.perform(get("/rest/deals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(deal.getDescription())));
    }

    @Test
    public void givenOffers_whenSendOffers_thenReturnJsonArray()
            throws Exception {
        Offer offer = new Offer ( "Offer 1", 2.0);

        List<Offer> deals = Collections.singletonList(offer);

        given(offerService.findAll()).willReturn(deals);

        mvc.perform(get("/rest/offers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(offer.getDescription())));
    }

    @Test
    public void givenCorrectUserInfo_whenValidateLogin_thenReturnJsonObject()
            throws Exception {
        User user = User.getBuilder(1,"Deborah", "Dor", "test123@test.com").build();
        user.setPassword("12345");


        given(userService.findUserByEmail(any(String.class))).willReturn(user);
        given(userService.passMatch(any(String.class), any(String.class))).willReturn(true);

        mvc.perform(get("/rest/login/test123@test.com/12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())));
    }

    @Test
    public void givenWrongUserPass_whenValidateLogin_thenReturnIncorrect()
            throws Exception {
        User user = User.getBuilder(1,"Deborah", "Dor", "test123@test.com").build();
        user.setPassword("12345");


        given(userService.findUserByEmail(any(String.class))).willReturn(user);
        given(userService.passMatch(any(String.class), any(String.class))).willReturn(false);

        mvc.perform(get("/rest/login/test123@test.com/12389")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("invalid"));
    }

    @Test
    public void givenWrongUserEmail_whenValidateLogin_thenReturnIncorrect()
            throws Exception {
        User user = User.getBuilder(1,"Deborah", "Dor", "test123@test.com").build();
        user.setPassword("12345");


        given(userService.findUserByEmail(any(String.class))).willReturn(null);
        given(userService.passMatch(any(String.class), any(String.class))).willReturn(false);

        mvc.perform(get("/rest/login/test123@test.com/12389")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("invalid"));
    }

    @Test
    public void givenNonExistingUser_whenValidateSignup_thenReturnOk()
            throws Exception {

        given(userService.findUserByEmail(any(String.class))).willReturn(null);

        mvc.perform(get("/rest/signup/test123@test.com/Deborah/Dor/12389")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    public void givenExistingUser_whenValidateSignup_thenReturnExistingUser()
            throws Exception {

        User user = User.getBuilder(1,"Deborah", "Dor", "test123@test.com").build();

        given(userService.findUserByEmail(any(String.class))).willReturn(user);

        mvc.perform(get("/rest/signup/test123@test.com/Deborah/Dor/12389")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Already existing user"));
    }

    @Test
    public void givenUserAndProduct_whenAddFavourite_thenRemoveFav()
            throws Exception {

        User user = User.getBuilder(1,"Deborah", "Dor", "test123@test.com").build();
        Category category = new Category("Cold Drinks");
        Product product = new Product(1, "milk", "semi",1.00, 50,category );


        given(userService.findById(anyInt())).willReturn(user);
        given(productService.findById(anyInt())).willReturn(product);

        mvc.perform(get("/rest/addFavourite/1/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(true, user.getFavProduct().contains(product));

    }

    @Test
    public void givenUserAndProduct_whenRemoveProduct_thenRemoveFav()
            throws Exception {

        User user = User.getBuilder(1,"Deborah", "Dor", "test123@test.com").build();
        Category category = new Category("Cold Drinks");
        Product product = new Product(1, "milk", "semi",1.00, 50,category );
        user.getFavProduct().add(product);



        given(userService.findById(anyInt())).willReturn(user);
        given(productService.findById(anyInt())).willReturn(product);

        mvc.perform(get("/rest/removeFavourite/1/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(false, user.getFavProduct().contains(product));

    }

    @Test
    public void givenOrderId_whenCheckStatus_thenReturnStatus()
            throws Exception {

        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.PENDING);


        given(orderService.findById(anyInt())).willReturn(order);

        mvc.perform(get("/rest/checkStatus/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Pending..."));

    }

    @Test
    public void givenOrderId_whenEditingOrder_thenUpdateOrder()
            throws Exception {

        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.PENDING);


        given(orderService.findById(anyInt())).willReturn(order);

        mvc.perform(get("/rest/editingOrder/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));


        assertEquals(OrderStatus.EDITING, order.getStatus());
    }

    @Test
    public void givenOrderId_whenDeleteOrder_thenRemoveOrder()
            throws Exception {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.PENDING);

        given(orderService.findById(anyInt())).willReturn(order);

        mvc.perform(get("/rest/deleteOrder/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    public void givenProductId_whenIncreaseQuantity_thenIncreaseQuantity()
            throws Exception {
        Category category = new Category("Cold Drinks");
        Product product = new Product(1, "milk", "semi",1.00, 50,category );


        given(productService.findById(anyInt())).willReturn(product);

        mvc.perform(get("/rest/increaseQuantity/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
        assertEquals(50+2, product.getQuantity());

    }

    @Test
    public void givenProductId_whenDecreaseQuantity_thenDecreaseQuantity()
            throws Exception {
        Category category = new Category("Cold Drinks");
        Product product = new Product(1, "milk", "semi",1.00, 50,category );


        given(productService.findById(anyInt())).willReturn(product);

        mvc.perform(get("/rest/decreaseQuantity/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
        assertEquals(50-2, product.getQuantity());

    }


}
