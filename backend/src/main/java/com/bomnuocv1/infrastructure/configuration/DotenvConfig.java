package com.bomnuocv1.infrastructure.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Slf4j
@Configuration
public class DotenvConfig {

    public static void loadDotenv() {
        try {
            Dotenv dotenv = null;
            // Check in current dir or parent dir
            if (new File(".env").exists()) {
                dotenv = Dotenv.configure().ignoreIfMissing().load();
            } else if (new File("../.env").exists()) {
                dotenv = Dotenv.configure().directory("../").ignoreIfMissing().load();
            } else if (new File("backend/.env").exists()) {
                dotenv = Dotenv.configure().directory("backend").ignoreIfMissing().load();
            } else {
                dotenv = Dotenv.configure().ignoreIfMissing().load();
            }

            if (dotenv != null) {
                dotenv.entries().forEach(entry -> {
                    if (System.getProperty(entry.getKey()) == null && System.getenv(entry.getKey()) == null) {
                        System.setProperty(entry.getKey(), entry.getValue());
                    }
                });
                log.info("Loaded environment variables from .env successfully.");
            }
        } catch (Exception e) {
            log.warn("Could not load .env file, relying on system environment variables: {}", e.getMessage());
        }
    }
}
