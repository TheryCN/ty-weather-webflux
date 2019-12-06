package com.github.therycn.tyweatherwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class TyWeatherWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TyWeatherWebfluxApplication.class, args);
    }

}
