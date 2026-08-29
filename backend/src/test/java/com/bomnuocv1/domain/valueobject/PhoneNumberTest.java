package com.bomnuocv1.domain.valueobject;

import com.bomnuocv1.domain.exception.InvalidPhoneNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhoneNumberTest {

    @Test
    @DisplayName("Should normalize standard Vietnamese phone numbers")
    void shouldNormalizePhoneNumber() {
        PhoneNumber phone1 = PhoneNumber.of("0912345678");
        assertEquals("0912345678", phone1.getValue());

        PhoneNumber phone2 = PhoneNumber.of("+84912345678");
        assertEquals("0912345678", phone2.getValue());

        PhoneNumber phone3 = PhoneNumber.of("0912 345 678");
        assertEquals("0912345678", phone3.getValue());
    }

    @Test
    @DisplayName("Should throw exception for invalid phone number format")
    void shouldThrowForInvalidPhone() {
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of("12345"));
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of("abcdefghij"));
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of(""));
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of(null));
    }
}
