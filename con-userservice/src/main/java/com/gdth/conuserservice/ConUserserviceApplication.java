package com.gdth.conuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ConUserserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConUserserviceApplication.class, args);
    }

}
