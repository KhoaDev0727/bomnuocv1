package com.bomnuocv1.infrastructure.persistence.adapter;

import com.bomnuocv1.domain.entity.Role;
import com.bomnuocv1.domain.repository.RoleRepository;
import com.bomnuocv1.infrastructure.persistence.entity.RoleJpaEntity;
import com.bomnuocv1.infrastructure.persistence.mapper.RolePersistenceMapper;
import com.bomnuocv1.infrastructure.persistence.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RolePersistenceMapper roleMapper;

    @Override
    public Role save(Role role) {
        RoleJpaEntity entity = roleMapper.toEntity(role);
        RoleJpaEntity saved = roleJpaRepository.save(entity);
        return roleMapper.toDomain(saved);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleJpaRepository.findById(id)
                .map(roleMapper::toDomain);
    }

    @Override
    public Optional<Role> findByCode(String code) {
        return roleJpaRepository.findByCode(code)
                .map(roleMapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return roleJpaRepository.existsByCode(code);
    }
}
