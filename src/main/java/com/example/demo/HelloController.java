package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping
    public String hello() {
        return "Hello, Heroku from Spring Boot! 🚀🚀🚀🚀🚀🚀 27.02.2025, deploy with Github tag v1";
    }
}