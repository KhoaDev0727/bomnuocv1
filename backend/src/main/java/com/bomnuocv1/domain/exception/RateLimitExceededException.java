package com.bomnuocv1.domain.exception;

public class RateLimitExceededException extends DomainException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
