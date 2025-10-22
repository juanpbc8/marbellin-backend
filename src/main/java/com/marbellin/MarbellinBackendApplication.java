package com.marbellin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MarbellinBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarbellinBackendApplication.class, args);
    }

}
