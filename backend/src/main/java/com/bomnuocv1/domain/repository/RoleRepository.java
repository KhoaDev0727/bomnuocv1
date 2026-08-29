package com.bomnuocv1.domain.repository;

import com.bomnuocv1.domain.entity.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(UUID id);

    Optional<Role> findByCode(String code);

    boolean existsByCode(String code);
}
