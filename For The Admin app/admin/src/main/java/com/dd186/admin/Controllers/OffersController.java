package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Offer.Offer;
import com.dd186.admin.Domain.Offer.OfferProduct;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Services.OfferService;
import com.dd186.admin.Services.ProductService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
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
            offer.setDescription(offer1.getDescription());
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
                                  @RequestParam(name = "value") double value,
                                  @RequestParam(name = "image", required = false)MultipartFile image) throws IOException, SQLException {
        Product product1 = productService.findById(prod1);
        Product product2 = productService.findById(prod2);
        Offer offer = new Offer();
        //if the item exists
        if (id != -1) {
            offer.setId(id);
        }

        if (!image.isEmpty()){
            Blob blob = new javax.sql.rowset.serial.SerialBlob(image.getBytes());
            offer.setImage(blob);
        } else if(id!=-1){
            Offer previous= offerService.findById(id);
            offer.setImage(previous.getImage());
        }

        offer.setDescription(desc);
        offer.setValue(value);

        if (prod3 != -1) {
            Product product3 = productService.findById(prod3);
            if ((prod1 == prod2) && prod1 == prod3){
                offer.addProduct(product1,3);
            } else if (prod1 == prod2){
                offer.addProduct(product1,2);
                offer.addProduct(product3,1);
            } else if (prod2 == prod3){
                offer.addProduct(product2,2);
                offer.addProduct(product1,1);
            } else if (prod1 == prod3){
                offer.addProduct(product1,2);
                offer.addProduct(product2,1);
            } else {
                offer.addProduct(product1,1);
                offer.addProduct(product2,1);
                offer.addProduct(product3,1);
            }
        }else{

            if (prod1 == prod2){
                offer.addProduct(product1,2);
            } else {
                offer.addProduct(product2,1);
                offer.addProduct(product1,1);
            }

        }
        offerService.save(offer);
        return new ModelAndView(new RedirectView("/main/offers"));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam(value="offerId", required=false, defaultValue="-1") int offerId) {
        Offer o = offerService.findById(offerId);
        if (o != null) {
            offerService.delete(o);
        }
        return new ModelAndView(new RedirectView("/main/offers"));
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void getImageAsByteArray(@RequestParam( name = "offerId") int id, HttpServletResponse response) throws IOException, SQLException {
        Offer offer = offerService.findById(id);
        InputStream in = offer.getImage().getBinaryStream();
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
}
