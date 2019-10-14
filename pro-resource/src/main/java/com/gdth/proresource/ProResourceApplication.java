package com.gdth.proresource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProResourceApplication.class, args);
    }

}
