package com.dd186.admin.Domain;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "FirstName")
//    @NotEmpty(message = "*Please provide your name")
    private String name;
    @Column(name = "LastName")
//    @NotEmpty(message = "*Please provide your last name")
    private String lastName;
    @Column(name = "Email")
//    @Email(message = "*Please provide a valid Email")
//    @NotEmpty(message = "*Please provide an email")
    private String email;
    @Column(name = "Password")
//    @Length(min = 5, message = "*Your password must have at least 5 characters")
//    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Column(name = "active")
    private int active;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String name, String lastName, String email, String password){
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(){

    }

    public @Length(min = 5, message = "*Your password must have at least 5 characters") @NotEmpty(message = "*Please provide your password") String getPassword() {
        return password;
    }

    public void setPassword(@Length(min = 5, message = "*Your password must have at least 5 characters") @NotEmpty(message = "*Please provide your password") String password) {
        this.password = password;
    }

    public void setRoles(HashSet<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public @NotEmpty(message = "*Please provide your name") String getName() {
        return name;
    }

    public @NotEmpty(message = "*Please provide your last name") String getLastName() {
        return lastName;
    }

    public @Email(message = "*Please provide a valid Email") @NotEmpty(message = "*Please provide an email") String getEmail() {
        return email;
    }

    public void setActive(int active) {
        this.active = active;
    }
}