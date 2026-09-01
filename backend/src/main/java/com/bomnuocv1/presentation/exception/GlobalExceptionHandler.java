package com.bomnuocv1.presentation.exception;

import com.bomnuocv1.domain.exception.DomainException;
import com.bomnuocv1.domain.exception.InvalidPhoneNumberException;
import com.bomnuocv1.domain.exception.InvalidPinException;
import com.bomnuocv1.domain.exception.InvalidTokenException;
import com.bomnuocv1.domain.exception.OtpVerificationException;
import com.bomnuocv1.domain.exception.PhoneAlreadyExistsException;
import com.bomnuocv1.domain.exception.PricingRuleNotFoundException;
import com.bomnuocv1.domain.exception.RoleNotFoundException;
import com.bomnuocv1.domain.exception.UnauthorizedException;
import com.bomnuocv1.domain.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handlePhoneAlreadyExists(PhoneAlreadyExistsException ex) {
        log.warn("Phone already exists: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "Phone Already Exists", ex.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, PricingRuleNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(DomainException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler({InvalidPinException.class, InvalidTokenException.class, UnauthorizedException.class})
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(DomainException ex) {
        log.warn("Unauthorized error: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage());
    }

    @ExceptionHandler({InvalidPhoneNumberException.class, OtpVerificationException.class, RoleNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequestDomain(DomainException ex) {
        log.warn("Domain validation error: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleGenericDomain(DomainException ex) {
        log.warn("Domain exception: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Domain Error", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("Validation failed: {}", errors);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Dữ liệu gửi lên không hợp lệ.")
                .validationErrors(errors)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
        log.error("Internal server error: ", ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Đã có lỗi xảy ra trên hệ thống. Vui lòng thử lại sau!"
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String error, String message) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .status(status.value())
                .error(error)
                .message(message)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
