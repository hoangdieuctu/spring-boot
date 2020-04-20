package com.hoangdieuctu.boot.elasticsearch.controller;

import com.hoangdieuctu.boot.elasticsearch.model.User;
import com.hoangdieuctu.boot.elasticsearch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping("/generate")
    public User generate() throws IOException {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("hoangdieuctu");
        return userService.index(user);
    }


    @ResponseBody
    @GetMapping("/search")
    public List<User> getByName(@RequestParam("name") String name) throws IOException {
        return userService.getByName(name);
    }
}
