package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@EnableWebSecurity
public class AuthApplication {

    void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }


    @Controller
    @ResponseBody
    static
    class SecurityControllers {
        @GetMapping("/secure")
        String home() {
            return "home";
        }

        @GetMapping("/admin")
        String admin() {
            return "admin";
        }


        @GetMapping("/other")
        String other() {
            return "home";
        }

    }

    @RestController
    static class WTFController {
        @GetMapping("/.well-known/appspecific/com.chrome.devtools.json")
        String wtf() {
            return "go login";
        }
    }

}
