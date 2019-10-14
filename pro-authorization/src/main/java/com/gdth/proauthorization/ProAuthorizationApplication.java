package com.gdth.proauthorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProAuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProAuthorizationApplication.class, args);
    }

}
