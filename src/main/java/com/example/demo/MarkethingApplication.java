package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing //monggo db 사용.
public class MarkethingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarkethingApplication.class, args);
    }

}
