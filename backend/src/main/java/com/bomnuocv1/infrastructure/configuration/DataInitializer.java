package com.bomnuocv1.infrastructure.configuration;

import com.bomnuocv1.infrastructure.persistence.entity.RoleJpaEntity;
import com.bomnuocv1.infrastructure.persistence.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public void run(String... args) {
        seedRoleIfNotExists("owner", "Chủ trạm", "Tài khoản chủ trạm bơm có đầy đủ quyền quản lý");
        seedRoleIfNotExists("staff", "Nhân viên", "Tài khoản nhân viên phụ hỗ trợ vận hành");
    }

    private void seedRoleIfNotExists(String code, String name, String description) {
        if (!roleJpaRepository.existsByCode(code)) {
            Instant now = Instant.now();
            RoleJpaEntity role = RoleJpaEntity.builder()
                    .id(UUID.randomUUID())
                    .code(code)
                    .name(name)
                    .description(description)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
            roleJpaRepository.save(role);
            log.info("Seeded default role: {} ({})", name, code);
        }
    }
}
