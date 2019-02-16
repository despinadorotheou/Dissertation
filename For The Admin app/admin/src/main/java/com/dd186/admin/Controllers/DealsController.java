package com.dd186.admin.Controllers;

import com.dd186.admin.Domain.Category;
import com.dd186.admin.Domain.Deal;
import com.dd186.admin.Domain.DealCategory;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Services.DealService;
import com.dd186.admin.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/main/deals")
public class DealsController {

    @Autowired
    private DealService dealService;
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView editDeal(@RequestParam(value = "dealId", required = false, defaultValue = "-1") int dealId) {
        ModelAndView modelAndView = new ModelAndView();
        Deal deal = new Deal();
        if (dealId > 0) {
            Deal deal2 = ((List<Deal>) dealService.findAll()).stream().filter(p -> (((Deal) p).getId() == dealId)).findAny().get();
            deal.setId(dealId);
            deal.setDealCategories(deal2.getDealCategories());
            deal.setValue(deal2.getValue());
            deal.setDescription(deal2.getDescription());
            modelAndView.addObject("deal", deal);
        }
        modelAndView.addObject("categories", productService.findAllCategories());
        modelAndView.setViewName("editDeal");
        return modelAndView;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView addChange(@RequestParam(name = "id", required = false, defaultValue = "-1") int id,
                                  @RequestParam(name = "category1") String cat1,
                                  @RequestParam(name = "category2") String cat2,
                                  @RequestParam(name = "category3", required = false, defaultValue = "-1") String cat3,
                                  @RequestParam(name = "description") String desc,
                                  @RequestParam(name = "value") double value) {
        ModelAndView modelAndView = new ModelAndView();
        Deal deal;
        Category category1 = productService.findByCategory(cat1);
        Category category2 = productService.findByCategory(cat2);
        if (!cat3.equals("-1")) {
            Category category3 = productService.findByCategory(cat3);
            if ((Objects.equals(cat1, cat2)) && Objects.equals(cat1, cat3)){
                deal = new Deal(new DealCategory(category1,3));
            } else if (Objects.equals(cat1, cat2)){
                deal = new Deal(new DealCategory(category1,2), new DealCategory(category3,1) );
            } else if (Objects.equals(cat2, cat3)){
                deal = new Deal(new DealCategory(category2,2), new DealCategory(category1,1) );
            } else if (Objects.equals(cat1, cat3)){
                deal = new Deal(new DealCategory(category1,2), new DealCategory(category2,1) );
            } else {
                deal = new Deal(new DealCategory(category1,1), new DealCategory(category2,1),  new DealCategory(category3,1) );
            }
        }else {
            if (Objects.equals(cat1, cat2)){
                deal = new Deal(new DealCategory(category1,2) );
            } else {
                deal = new Deal(new DealCategory(category1,1), new DealCategory(category2,1) );
            }
        }

        if (id != -1) {
            deal.setId(id);
        }
        deal.setDescription(desc);
        deal.setValue(value);
        dealService.save(deal);
        modelAndView.addObject("deals", (List<Deal>) dealService.findAll());
        modelAndView.setViewName("dealsPage");
        return modelAndView;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam(value="dealId", required=false, defaultValue="-1") int dealId) {
        ModelAndView modelAndView = new ModelAndView();
        Deal d = dealService.findById(dealId);
        if (d != null) {
            dealService.delete(d);
        }
        modelAndView.addObject("deals",(List<Deal>)dealService.findAll());
        modelAndView.setViewName("dealsPage");
        return modelAndView;
    }
}
