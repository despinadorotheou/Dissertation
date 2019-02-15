package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Deal;
import com.dd186.admin.Domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("offerRepository")
public interface OfferRepository  extends JpaRepository<Offer, Integer> {

}
