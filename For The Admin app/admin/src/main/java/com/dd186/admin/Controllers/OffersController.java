package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Category;
import com.dd186.admin.Domain.Offer;
import com.dd186.admin.Domain.OfferProduct;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Services.OfferService;
import com.dd186.admin.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/main/offers")
public class OffersController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OfferService offerService;

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView editOffer(@RequestParam(value = "offerId", required = false, defaultValue = "-1") int offerId) {
        ModelAndView modelAndView = new ModelAndView();
        Offer offer = new Offer();
        if (offerId > 0) {
            Offer offer1 = ((List<Offer>) offerService.findAll()).stream().filter(p -> (((Offer) p).getId() == offerId)).findAny().get();
            offer.setId(offerId);
            offer.setOfferProducts(offer1.getOfferProducts());
            offer.setValue(offer1.getValue());
            modelAndView.addObject("offer", offer);
        }
        modelAndView.addObject("products", productService.findAll());
        modelAndView.setViewName("editOffer");
        return modelAndView;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView addChange(@RequestParam(name = "id", required = false, defaultValue = "-1") int id,
                                  @RequestParam(name = "product1") int prod1,
                                  @RequestParam(name = "product2") int prod2,
                                  @RequestParam(name = "product3", required = false, defaultValue = "-1") int prod3,
                                  @RequestParam(name = "description") String desc,
                                  @RequestParam(name = "value") double value) {
        ModelAndView modelAndView = new ModelAndView();
        Product product1 = productService.findById(prod1);
        Product product2 = productService.findById(prod2);
        Offer offer;
        if (prod3 != -1) {
            Product product3 = productService.findById(prod3);
            if ((prod1 == prod2) && prod1 == prod3){
                 offer = new Offer(new OfferProduct(product1,3));
            } else if (prod1 == prod2){
                 offer = new Offer(new OfferProduct(product1,2), new OfferProduct(product3,1) );
            } else if (prod2 == prod3){
                 offer = new Offer(new OfferProduct(product2,2), new OfferProduct(product1,1) );
            } else if (prod1 == prod3){
                 offer = new Offer(new OfferProduct(product1,2), new OfferProduct(product2,1) );
            } else {
                 offer = new Offer(new OfferProduct(product1,1), new OfferProduct(product2,1),  new OfferProduct(product3,1) );
            }
        }else{

            if (prod1 == prod2){
                offer = new Offer(new OfferProduct(product1,2) );
            } else {
                offer = new Offer(new OfferProduct(product1,1), new OfferProduct(product2,1) );
            }

        }
        if (id != -1) {
            offer.setId(id);
        }
        offer.setDescription(desc);
        offer.setValue(value);
        offerService.save(offer);
        modelAndView.addObject("offers", (List<Offer>) offerService.findAll());
        modelAndView.setViewName("offersPage");
        return modelAndView;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam(value="offerId", required=false, defaultValue="-1") int offerId) {
        ModelAndView modelAndView = new ModelAndView();
        Offer o = offerService.findById(offerId);
        if (o != null) {
            o.setOfferProducts(null);
            offerService.delete(o);
        }
        modelAndView.addObject("offers", (List<Offer>) offerService.findAll());
        modelAndView.setViewName("offersPage");
        return modelAndView;
    }
}
