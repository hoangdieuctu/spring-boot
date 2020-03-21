package com.hoangdieuctu.boot.docker;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ServerApplication {

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "Say Hello!";
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
