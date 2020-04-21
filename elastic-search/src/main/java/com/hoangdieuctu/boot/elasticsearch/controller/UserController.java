package com.hoangdieuctu.boot.elasticsearch.controller;

import com.hoangdieuctu.boot.elasticsearch.model.User;
import com.hoangdieuctu.boot.elasticsearch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping("/generate")
    public User generate() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("hoangdieuctu");

        return userService.save(user);
    }

    @ResponseBody
    @GetMapping("/all")
    public List<User> getAll() {
        return userService.getAll();
    }

    @ResponseBody
    @GetMapping("/search")
    public List<User> getByName(@RequestParam("name") String name) {
        return userService.getByName(name);
    }

    @ResponseBody
    @GetMapping("/update/{id}")
    public User update(@PathVariable("id") String id, @RequestParam("name") String name) {
        return userService.update(id, name);
    }

    @ResponseBody
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable("id") String id) {
        userService.delete(id);
    }
}
