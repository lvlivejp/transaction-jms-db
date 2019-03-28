package com.example.transactionjms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class TransactionJmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionJmsApplication.class, args);
    }

}
