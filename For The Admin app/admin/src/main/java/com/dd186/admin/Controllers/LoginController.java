package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Order.Order;
import com.dd186.admin.Domain.Order.OrderStatus;
import com.dd186.admin.Domain.User;
import com.dd186.admin.Services.OrderService;
import com.dd186.admin.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    OrderService orderService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value="/main", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Order> pendingOrders = new ArrayList<>();
        for (Order o:orderService.getAll()) {
            if (o.getStatus() != OrderStatus.COLLECTED){
                pendingOrders.add(o);
            }
        }
        modelAndView.addObject("orders",(List<Order>)pendingOrders);
        modelAndView.setViewName("ordersPage");
        return modelAndView;
    }


}