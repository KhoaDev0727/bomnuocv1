package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.AuthResult;
import com.bomnuocv1.application.port.in.RegisterUserCommand;
import com.bomnuocv1.application.port.out.CachePort;
import com.bomnuocv1.application.port.out.OtpServicePort;
import com.bomnuocv1.application.port.out.PasswordEncoderPort;
import com.bomnuocv1.application.port.out.TokenProviderPort;
import com.bomnuocv1.domain.entity.Role;
import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.exception.PhoneAlreadyExistsException;
import com.bomnuocv1.domain.repository.RoleRepository;
import com.bomnuocv1.domain.repository.UserRepository;
import com.bomnuocv1.domain.valueobject.PhoneNumber;
import com.bomnuocv1.domain.valueobject.PinCode;
import com.bomnuocv1.domain.valueobject.TokenPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @Mock
    private CachePort cachePort;

    @Mock
    private OtpServicePort otpServicePort;

    @InjectMocks
    private RegisterUserService registerUserService;

    @Test
    @DisplayName("Register should succeed for new phone number")
    void shouldRegisterUserSuccessfully() {
        RegisterUserCommand command = RegisterUserCommand.builder()
                .phoneNumber("0912345678")
                .pinCode("1234")
                .fullName("Nguyễn Văn Chủ Trạm")
                .build();

        Role ownerRole = Role.createOwnerRole();
        User savedUser = User.createNewUser(
                PhoneNumber.of("0912345678"),
                "$2a$10$encodedHash",
                "Nguyễn Văn Chủ Trạm",
                ownerRole
        );

        TokenPair tokenPair = TokenPair.builder()
                .accessToken("mock-access-token")
                .refreshToken("mock-refresh-token")
                .tokenType("Bearer")
                .expiresInMs(86400000L)
                .build();

        when(userRepository.existsByPhoneNumber(any(PhoneNumber.class))).thenReturn(false);
        when(roleRepository.findByCode("owner")).thenReturn(Optional.of(ownerRole));
        when(passwordEncoderPort.encode(any(PinCode.class))).thenReturn("$2a$10$encodedHash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenProviderPort.generateTokenPair(any(User.class))).thenReturn(tokenPair);
        when(tokenProviderPort.getRefreshTokenExpirationMs()).thenReturn(2592000000L);

        AuthResult result = registerUserService.execute(command);

        assertNotNull(result);
        assertEquals("0912345678", result.getUser().getPhoneNumber());
        assertEquals("Nguyễn Văn Chủ Trạm", result.getUser().getFullName());
        assertEquals("owner", result.getUser().getRoleCode());
        verify(cachePort).saveRefreshToken(any(), any(), any());
    }

    @Test
    @DisplayName("Register should throw PhoneAlreadyExistsException when phone exists")
    void shouldThrowWhenPhoneAlreadyRegistered() {
        RegisterUserCommand command = RegisterUserCommand.builder()
                .phoneNumber("0912345678")
                .pinCode("1234")
                .fullName("Nguyễn Văn Chủ Trạm")
                .build();

        when(userRepository.existsByPhoneNumber(any(PhoneNumber.class))).thenReturn(true);

        assertThrows(PhoneAlreadyExistsException.class, () -> registerUserService.execute(command));
    }
}
