package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Order.Order;
import com.dd186.admin.Domain.Order.OrderStatus;
import com.dd186.admin.Domain.User;
import com.dd186.admin.Services.OrderService;
import com.dd186.admin.Services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerIntegrationTest{

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    public static class MockSecurityContext implements SecurityContext {

        private static final long serialVersionUID = -1386535243513362694L;

        private Authentication authentication;

        public MockSecurityContext(Authentication authentication) {
            this.authentication = authentication;
        }

        @Override
        public Authentication getAuthentication() {
            return this.authentication;
        }

        @Override
        public void setAuthentication(Authentication authentication) {
            this.authentication = authentication;
        }
    }

    @Test
    public void testDirectToLoginPage()
            throws Exception {

        mvc.perform(get("/login")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }

    @Test
    public void testDirectToLoginPage2()
            throws Exception {

        mvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }

    @Test
    @WithMockUser(value = "dd186@student.le.ac.uk")
    public void testDirectToMainPage()
            throws Exception {

        User user = User.getBuilder(1,"Deborah", "Dor", "test123@test.com").build();
        user.setPassword("12345");
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.PENDING);


        given(userService.findUserByEmail(any(String.class))).willReturn(user);
        given(orderService.getAll()).willReturn(new ArrayList<>(Collections.singleton(order)));


        mvc.perform(get("/main")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute("orders", new ArrayList<>(Collections.singleton(order))))
                .andExpect(view().name("ordersPage"));

    }

}
