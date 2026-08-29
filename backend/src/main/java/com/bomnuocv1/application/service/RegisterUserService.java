package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.AuthResult;
import com.bomnuocv1.application.dto.TokenResult;
import com.bomnuocv1.application.dto.UserResult;
import com.bomnuocv1.application.port.in.RegisterUserCommand;
import com.bomnuocv1.application.port.out.CachePort;
import com.bomnuocv1.application.port.out.OtpServicePort;
import com.bomnuocv1.application.port.out.PasswordEncoderPort;
import com.bomnuocv1.application.port.out.TokenProviderPort;
import com.bomnuocv1.application.usecase.RegisterUserUseCase;
import com.bomnuocv1.domain.entity.Role;
import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.exception.OtpVerificationException;
import com.bomnuocv1.domain.exception.PhoneAlreadyExistsException;
import com.bomnuocv1.domain.exception.RoleNotFoundException;
import com.bomnuocv1.domain.repository.RoleRepository;
import com.bomnuocv1.domain.repository.UserRepository;
import com.bomnuocv1.domain.valueobject.PhoneNumber;
import com.bomnuocv1.domain.valueobject.PinCode;
import com.bomnuocv1.domain.valueobject.TokenPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;
    private final CachePort cachePort;
    private final OtpServicePort otpServicePort;

    @Override
    @Transactional
    public AuthResult execute(RegisterUserCommand command) {
        PhoneNumber phoneNumber = PhoneNumber.of(command.getPhoneNumber());
        PinCode pinCode = PinCode.of(command.getPinCode());

        // 1. Verify phone not already registered
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PhoneAlreadyExistsException("Số điện thoại này đã được đăng ký tài khoản.");
        }

        // 2. Verify Firebase ID Token
        if (command.getFirebaseIdToken() != null && !command.getFirebaseIdToken().trim().isEmpty()) {
            boolean valid = otpServicePort.verifyFirebaseIdToken(command.getFirebaseIdToken(), phoneNumber);
            if (!valid) {
                throw new OtpVerificationException("Xác thực Firebase ID Token không hợp lệ cho số điện thoại.");
            }
        }

        // 3. Resolve Role (default to 'owner' for MVP)
        String roleCode = (command.getRoleCode() != null && !command.getRoleCode().trim().isEmpty())
                ? command.getRoleCode().trim().toLowerCase()
                : "owner";

        Role role = roleRepository.findByCode(roleCode)
                .orElseGet(() -> {
                    if ("owner".equalsIgnoreCase(roleCode)) {
                        return roleRepository.save(Role.createOwnerRole());
                    }
                    throw new RoleNotFoundException("Không tìm thấy vai trò người dùng: " + roleCode);
                });

        // 4. Encode PIN
        String pinHash = passwordEncoderPort.encode(pinCode);

        // 5. Create and persist domain User
        User user = User.createNewUser(phoneNumber, pinHash, command.getFullName(), role);
        User savedUser = userRepository.save(user);

        // 6. Generate Tokens
        TokenPair tokenPair = tokenProviderPort.generateTokenPair(savedUser);

        // 7. Save refresh token to Redis
        long refreshTtlMs = tokenProviderPort.getRefreshTokenExpirationMs();
        cachePort.saveRefreshToken(savedUser.getId(), tokenPair.getRefreshToken(), Duration.ofMillis(refreshTtlMs));

        log.info("User registered successfully with phone: {} and role: {}", savedUser.getPhoneNumber().getValue(), role.getCode());

        // 8. Map to AuthResult
        UserResult userResult = UserResult.builder()
                .id(savedUser.getId())
                .phoneNumber(savedUser.getPhoneNumber().getValue())
                .fullName(savedUser.getFullName())
                .roleCode(savedUser.getRole().getCode())
                .roleName(savedUser.getRole().getName())
                .active(savedUser.isActive())
                .createdAt(savedUser.getCreatedAt())
                .build();

        TokenResult tokenResult = TokenResult.builder()
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .tokenType(tokenPair.getTokenType())
                .expiresInMs(tokenPair.getExpiresInMs())
                .build();

        return AuthResult.builder()
                .user(userResult)
                .tokens(tokenResult)
                .build();
    }
}
