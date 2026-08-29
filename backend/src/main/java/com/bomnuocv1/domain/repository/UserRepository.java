package com.bomnuocv1.domain.repository;

import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.valueobject.PhoneNumber;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByPhoneNumber(PhoneNumber phoneNumber);

    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
}
