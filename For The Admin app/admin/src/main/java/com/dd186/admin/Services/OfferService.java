package com.dd186.admin.Services;

import com.dd186.admin.Domain.Offer;
import com.dd186.admin.Repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("offerService")
public class OfferService {

    OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository){
        this.offerRepository = offerRepository;
    }

    public List<Offer> findAll(){
        return offerRepository.findAll();
    }

    public void save(Offer offer){
        offerRepository.save(offer);
    }

    public Offer findById(int id){
        return offerRepository.findById(id);
    }

    public void delete(Offer offer){
        offerRepository.delete(offer);
    }
}
