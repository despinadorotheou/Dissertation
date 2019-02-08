package com.dd186.admin.Services;

import com.dd186.admin.Domain.Product;
import com.dd186.admin.Domain.Role;
import com.dd186.admin.Domain.User;
import com.dd186.admin.Repositories.RoleRepository;
import com.dd186.admin.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(int id) {
        return userRepository.findById(id);
    }

    public void saveUserCustom(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
//        user.setRoles(userRole);
        user.setFavProduct(new HashSet<Product>());
        userRepository.save(user);
    }

    public boolean passMatch(String raw, String bcrypted){
        return bCryptPasswordEncoder.matches(raw,bcrypted);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }


}