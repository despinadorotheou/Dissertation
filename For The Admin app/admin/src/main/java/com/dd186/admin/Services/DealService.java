package com.dd186.admin.Services;

import com.dd186.admin.Domain.Deal;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Repositories.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dealService")
public class DealService {

    DealRepository dealRepository;

    @Autowired
    public DealService(DealRepository dealRepository){
        this.dealRepository = dealRepository;
    }

    public List<Deal> findAll(){
        return dealRepository.findAll();
    }

    public void save(Deal deal){
        dealRepository.save(deal);
    }

    public Deal findById(int id){
        return dealRepository.findById(id);
    }

    public void delete(Deal deal){
        dealRepository.delete(deal);
    }



}
