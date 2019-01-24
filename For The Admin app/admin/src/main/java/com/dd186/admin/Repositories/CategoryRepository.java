package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Category;
import com.dd186.admin.Domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CategoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByCategory(String name);
}
