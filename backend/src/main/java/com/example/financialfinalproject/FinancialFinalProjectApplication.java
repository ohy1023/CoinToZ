package com.example.financialfinalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FinancialFinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialFinalProjectApplication.class, args);
    }

}
