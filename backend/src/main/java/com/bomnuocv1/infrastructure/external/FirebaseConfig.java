package com.bomnuocv1.infrastructure.external;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    private final ResourceLoader resourceLoader;

    @Value("${firebase.credentials.path:classpath:serviceAccountKey.json}")
    private String credentialsPath;

    @Value("${firebase.project-id:bomnuocv1}")
    private String projectId;

    public FirebaseConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccountStream = resolveCredentialsStream();
                if (serviceAccountStream != null) {
                    try (serviceAccountStream) {
                        FirebaseOptions.Builder builder = FirebaseOptions.builder()
                                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream));

                        if (projectId != null && !projectId.trim().isEmpty()) {
                            builder.setProjectId(projectId.trim());
                        }

                        FirebaseApp.initializeApp(builder.build());
                        log.info("Firebase Admin SDK initialized successfully with credentials from: {}", credentialsPath);
                    }
                } else {
                    log.info("No Firebase credentials found at [{}]. Fallback / dev OTP authentication mode enabled.", credentialsPath);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to initialize Firebase Admin SDK, falling back: {}", e.getMessage());
        }
    }

    private InputStream resolveCredentialsStream() {
        if (credentialsPath == null || credentialsPath.trim().isEmpty()) {
            return tryDefaultLocations();
        }

        String trimmedPath = credentialsPath.trim();
        try {
            Resource resource = resourceLoader.getResource(trimmedPath);
            if (resource.exists()) {
                return resource.getInputStream();
            }

            if (!trimmedPath.startsWith("classpath:") && !trimmedPath.startsWith("file:")) {
                Resource fileRes = resourceLoader.getResource("file:" + trimmedPath);
                if (fileRes.exists()) {
                    return fileRes.getInputStream();
                }
            }
        } catch (Exception e) {
            log.debug("Could not resolve credentials from path [{}]: {}", trimmedPath, e.getMessage());
        }

        return tryDefaultLocations();
    }

    private InputStream tryDefaultLocations() {
        String[] defaultLocations = {
            "classpath:serviceAccountKey.json",
            "file:serviceAccountKey.json",
            "file:./serviceAccountKey.json",
            "file:../serviceAccountKey.json"
        };

        for (String location : defaultLocations) {
            try {
                Resource resource = resourceLoader.getResource(location);
                if (resource.exists()) {
                    log.info("Resolved Firebase credentials from fallback location: {}", location);
                    return resource.getInputStream();
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
