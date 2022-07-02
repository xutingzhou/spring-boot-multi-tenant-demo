package com.example.multitenancy.controller;

import com.example.multitenancy.tenant.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping()
@Transactional
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "users/{id}")
    public String getActor(@PathVariable("id") long id) {
        return userService.getUser(id).getName();
    }

    @PostMapping(value = "/users/{user}")
    public void add(@PathVariable("user") String user) {
        userService.addUser(user);
    }

}