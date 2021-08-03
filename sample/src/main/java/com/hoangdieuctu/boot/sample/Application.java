package com.hoangdieuctu.boot.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    @ResponseBody
    @GetMapping("/foo/hello")
    public String fooHello() {
        logger.info("Say hello...");
        return "Foo Say Hello!";
    }

    @ResponseBody
    @GetMapping("/bar/hello")
    public String barHello() {
        logger.info("Say hello...");
        return "Bar Say Hello!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
