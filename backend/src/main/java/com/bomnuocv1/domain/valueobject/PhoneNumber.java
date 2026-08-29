package com.bomnuocv1.domain.valueobject;

import com.bomnuocv1.domain.exception.InvalidPhoneNumberException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public class PhoneNumber implements Serializable {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[0-9]{9,10}$");

    private final String value;

    public PhoneNumber(String rawPhone) {
        if (rawPhone == null || rawPhone.trim().isEmpty()) {
            throw new InvalidPhoneNumberException("Phone number cannot be empty.");
        }
        String cleaned = rawPhone.replaceAll("[\\s\\-\\(\\)]", "");
        if (!PHONE_PATTERN.matcher(cleaned).matches()) {
            throw new InvalidPhoneNumberException("Invalid phone number format: " + rawPhone);
        }
        // Normalize to standard 0-prefixed format
        if (cleaned.startsWith("+84")) {
            cleaned = "0" + cleaned.substring(3);
        }
        this.value = cleaned;
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
