package com.example.goodreads_finalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class GoodReadsFinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodReadsFinalProjectApplication.class, args);

            }

}
