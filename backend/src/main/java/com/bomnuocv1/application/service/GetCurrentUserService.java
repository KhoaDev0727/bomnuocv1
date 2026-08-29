package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.UserResult;
import com.bomnuocv1.application.usecase.GetCurrentUserUseCase;
import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.exception.UserNotFoundException;
import com.bomnuocv1.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCurrentUserService implements GetCurrentUserUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResult execute(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy thông tin người dùng."));

        return UserResult.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber().getValue())
                .fullName(user.getFullName())
                .roleCode(user.getRole().getCode())
                .roleName(user.getRole().getName())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
