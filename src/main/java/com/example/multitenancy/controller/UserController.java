package com.example.multitenancy.controller;

import com.example.multitenancy.tenant.model.User;
import com.example.multitenancy.tenant.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping(value = "/user")
@Transactional
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/{id}")
    public String getActor(@PathVariable("id") long id) {
        return userRepository.findById(id).orElse(new User().setName("a")).getName();
    }

}