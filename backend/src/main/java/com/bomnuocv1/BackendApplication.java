package com.bomnuocv1;

import com.bomnuocv1.infrastructure.configuration.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        DotenvConfig.loadDotenv();
        SpringApplication.run(BackendApplication.class, args);
    }
}
