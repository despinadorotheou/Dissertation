package com.dd186.admin.Controllers;
import com.dd186.admin.Domain.*;
import com.dd186.admin.Domain.Deal.Deal;
import com.dd186.admin.Domain.Offer.Offer;
import com.dd186.admin.Domain.Order.Order;
import com.dd186.admin.Domain.Order.OrderStatus;
import com.dd186.admin.Services.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/main")
public class OrderController {


    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @Autowired
    OrderService orderService;

    @Autowired
    private DealService dealService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "/order/ready", method = RequestMethod.GET)
    public ModelAndView orderReady(@RequestParam(value="orderId") int orderId) throws JSONException {
        Order order = orderService.findById(orderId);
        order.setStatus(OrderStatus.READY);
        orderService.save(order);
        sendNotification("Your order #"+ orderId + " is ready for collection!", order.getUserid() );
        return new ModelAndView(new RedirectView("/main"));

    }

    @RequestMapping(value = "/order/collected", method = RequestMethod.GET)
    public ModelAndView orderCollected(@RequestParam(value="orderId") int orderId) throws JSONException {
        Order order = orderService.findById(orderId);
        order.setStatus(OrderStatus.COLLECTED);
        orderService.save(order);
        sendNotification("Your order #"+ orderId + " was collected!", order.getUserid() );
        return new ModelAndView(new RedirectView("/main"));
    }

    @RequestMapping(value = "/deals", method = RequestMethod.GET)
    public ModelAndView dealPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("deals",(List<Deal>)dealService.findAll());
        modelAndView.setViewName("dealsPage");
        return modelAndView;
    }

    @RequestMapping(value = "/offers", method = RequestMethod.GET)
    public ModelAndView offerPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("offers",(List<Offer>)offerService.findAll());
        modelAndView.setViewName("offersPage");
        return modelAndView;
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ModelAndView productPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products",(List<Product>)productService.findAll());
        modelAndView.setViewName("productsPage");
        return modelAndView;
    }

    private void sendNotification(String msg, int topic) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("to", "/topics/" + topic);
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", "UniFood");
        notification.put("body", msg );

        body.put("notification", notification);

        /*
         {
         "notification": {
         "title": "UniFood",
         "body": "Happy Message!"
         },
         "to": "/topics/userID",
         "priority": "high"
         }
         */

        HttpEntity<String> request = new HttpEntity<>(body.toString());
        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();
            System.out.println(new ResponseEntity<>(firebaseResponse, HttpStatus.OK));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST));
            e.printStackTrace();
        }
    }

}
