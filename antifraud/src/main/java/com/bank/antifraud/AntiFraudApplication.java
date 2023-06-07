package com.bank.antifraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.bank.common", "com.bank.antifraud"})
public class AntiFraudApplication {
    public static void main(String[] args) {
        SpringApplication.run(AntiFraudApplication.class, args);
    }
}
