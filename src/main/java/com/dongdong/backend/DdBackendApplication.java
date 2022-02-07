package com.dongdong.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableJpaRepositories
public class DdBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DdBackendApplication.class, args);
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong!";
    }

}
