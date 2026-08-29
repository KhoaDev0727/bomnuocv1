package com.bomnuocv1.domain.valueobject;

import com.bomnuocv1.domain.exception.InvalidPinException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PinCodeTest {

    @Test
    @DisplayName("Should accept valid 4 digit numeric PIN")
    void shouldAcceptValidPin() {
        PinCode pin4 = PinCode.of("1234");
        assertEquals("1234", pin4.getValue());
    }

    @Test
    @DisplayName("Should throw exception for PIN with letters or length != 4")
    void shouldThrowForInvalidPin() {
        assertThrows(InvalidPinException.class, () -> PinCode.of("123")); // < 4
        assertThrows(InvalidPinException.class, () -> PinCode.of("12345")); // > 4
        assertThrows(InvalidPinException.class, () -> PinCode.of("123456")); // > 4
        assertThrows(InvalidPinException.class, () -> PinCode.of("12ab")); // letters
        assertThrows(InvalidPinException.class, () -> PinCode.of(null));
        assertThrows(InvalidPinException.class, () -> PinCode.of(""));
    }
}
