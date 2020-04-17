package com.gmail.ezlotnikova.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.gmail.ezlotnikova.web",
        "com.gmail.ezlotnikova.service",
        "com.gmail.ezlotnikova.repository"},
        exclude = UserDetailsServiceAutoConfiguration.class)
public class WebModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebModuleApplication.class, args);
    }

}