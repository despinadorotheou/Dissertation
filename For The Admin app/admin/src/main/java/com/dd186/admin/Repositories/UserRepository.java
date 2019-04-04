package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Order;
import com.dd186.admin.Domain.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findById(int id);
}