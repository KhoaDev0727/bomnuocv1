package com.bomnuocv1.domain.valueobject;

import com.bomnuocv1.domain.exception.InvalidPinException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public class PinCode implements Serializable {

    private static final Pattern PIN_PATTERN = Pattern.compile("^[0-9]{4}$");

    private final String value;

    public PinCode(String rawPin) {
        if (rawPin == null || rawPin.trim().isEmpty()) {
            throw new InvalidPinException("Mã PIN không được để trống.");
        }
        String trimmed = rawPin.trim();
        if (!PIN_PATTERN.matcher(trimmed).matches()) {
            throw new InvalidPinException("Mã PIN phải gồm đúng 4 chữ số.");
        }
        this.value = trimmed;
    }

    public static PinCode of(String value) {
        return new PinCode(value);
    }

    @Override
    public String toString() {
        return "****";
    }
}
