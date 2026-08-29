package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.AuthResult;
import com.bomnuocv1.application.port.in.LoginWithPinCommand;
import com.bomnuocv1.application.port.out.CachePort;
import com.bomnuocv1.application.port.out.PasswordEncoderPort;
import com.bomnuocv1.application.port.out.TokenProviderPort;
import com.bomnuocv1.domain.entity.Role;
import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.exception.InvalidPinException;
import com.bomnuocv1.domain.exception.UserNotFoundException;
import com.bomnuocv1.domain.repository.UserRepository;
import com.bomnuocv1.domain.valueobject.PhoneNumber;
import com.bomnuocv1.domain.valueobject.PinCode;
import com.bomnuocv1.domain.valueobject.TokenPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginWithPinServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @Mock
    private CachePort cachePort;

    @InjectMocks
    private LoginWithPinService loginWithPinService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        Role role = Role.createOwnerRole();
        mockUser = User.createNewUser(
                PhoneNumber.of("0912345678"),
                "$2a$10$encodedPinHashExample",
                "Nguyễn Văn Chủ Trạm",
                role
        );
    }

    @Test
    @DisplayName("Login should succeed when phone exists and PIN matches")
    void shouldLoginSuccessfully() {
        LoginWithPinCommand command = LoginWithPinCommand.builder()
                .phoneNumber("0912345678")
                .pinCode("1234")
                .build();

        TokenPair tokenPair = TokenPair.builder()
                .accessToken("mock-access-token")
                .refreshToken("mock-refresh-token")
                .tokenType("Bearer")
                .expiresInMs(86400000L)
                .build();

        when(userRepository.findByPhoneNumber(any(PhoneNumber.class))).thenReturn(Optional.of(mockUser));
        when(passwordEncoderPort.matches(eq(PinCode.of("1234")), eq(mockUser.getPinHash()))).thenReturn(true);
        when(tokenProviderPort.generateTokenPair(mockUser)).thenReturn(tokenPair);
        when(tokenProviderPort.getRefreshTokenExpirationMs()).thenReturn(2592000000L);

        AuthResult result = loginWithPinService.execute(command);

        assertNotNull(result);
        assertEquals("0912345678", result.getUser().getPhoneNumber());
        assertEquals("Nguyễn Văn Chủ Trạm", result.getUser().getFullName());
        assertEquals("mock-access-token", result.getTokens().getAccessToken());
        verify(cachePort).saveRefreshToken(eq(mockUser.getId()), eq("mock-refresh-token"), any());
    }

    @Test
    @DisplayName("Login should throw UserNotFoundException when phone is not registered")
    void shouldThrowWhenUserNotFound() {
        LoginWithPinCommand command = LoginWithPinCommand.builder()
                .phoneNumber("0987654321")
                .pinCode("1234")
                .build();

        when(userRepository.findByPhoneNumber(any(PhoneNumber.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> loginWithPinService.execute(command));
    }

    @Test
    @DisplayName("Login should throw InvalidPinException when PIN is incorrect")
    void shouldThrowWhenPinIsIncorrect() {
        LoginWithPinCommand command = LoginWithPinCommand.builder()
                .phoneNumber("0912345678")
                .pinCode("9999")
                .build();

        when(userRepository.findByPhoneNumber(any(PhoneNumber.class))).thenReturn(Optional.of(mockUser));
        when(passwordEncoderPort.matches(eq(PinCode.of("9999")), eq(mockUser.getPinHash()))).thenReturn(false);

        assertThrows(InvalidPinException.class, () -> loginWithPinService.execute(command));
    }
}
