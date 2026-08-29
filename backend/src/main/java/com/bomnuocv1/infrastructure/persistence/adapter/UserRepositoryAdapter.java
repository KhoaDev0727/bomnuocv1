package com.bomnuocv1.infrastructure.persistence.adapter;

import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.repository.UserRepository;
import com.bomnuocv1.domain.valueobject.PhoneNumber;
import com.bomnuocv1.infrastructure.persistence.entity.UserJpaEntity;
import com.bomnuocv1.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.bomnuocv1.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userMapper;

    @Override
    public User save(User user) {
        UserJpaEntity entity = userMapper.toEntity(user);
        UserJpaEntity saved = userJpaRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByPhoneNumber(PhoneNumber phoneNumber) {
        return userJpaRepository.findByPhoneNumber(phoneNumber.getValue())
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
        return userJpaRepository.existsByPhoneNumber(phoneNumber.getValue());
    }
}
