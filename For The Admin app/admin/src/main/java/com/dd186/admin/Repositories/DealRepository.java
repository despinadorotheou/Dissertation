package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Deal.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("dealRepository")
public interface DealRepository extends JpaRepository<Deal, Integer> {
    Deal findById(int id);
}
