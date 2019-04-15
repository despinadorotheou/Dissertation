package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Offer.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("offerRepository")
public interface OfferRepository  extends JpaRepository<Offer, Integer> {
    Offer findById(int id);
}
