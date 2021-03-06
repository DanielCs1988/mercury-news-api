package com.danielcs.mercurynews;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MercuryNewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercuryNewsApplication.class, args);
    }

    @Bean
    public Gson provideJsonConverter() {
        return new Gson();
    }
}
