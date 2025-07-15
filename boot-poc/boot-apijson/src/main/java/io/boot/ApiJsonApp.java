package io.boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ApiJsonApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ApiJsonApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
