package com.bncrypted.authenticator.util.otp;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TotpHelperTest {

    private static final String VALID_TOTP_KEY = "IFAUCQI=";
    private final OtpHelper otpHelper = new TotpHelper();

    @Test
    void shouldIssueTotpWithValidBase32EncodedKey() {
        String totp = otpHelper.issueOtp(VALID_TOTP_KEY);
        assertNotNull(totp);
        assertTrue(Pattern.compile("[0-9]").matcher(totp).find(), "totp should only contain digits 0-9");
    }

    @Test
    void shouldNotIssueTotpWithInvalidBase32EncodedKey() {
        assertThrows(IllegalArgumentException.class, () -> otpHelper.issueOtp("invalid"));
    }

    @Test
    void shouldValidateValidOtp() {
        String validOtp = otpHelper.issueOtp(VALID_TOTP_KEY);
        assertTrue(otpHelper.isOtpValid(VALID_TOTP_KEY, validOtp));
    }

    @Test
    void shouldNotValidateInvalidOtp() {
        String validOtp = otpHelper.issueOtp(VALID_TOTP_KEY);
        String invalidOtp = String.valueOf(Integer.parseInt(validOtp) + 1 % 1000000);
        assertFalse(otpHelper.isOtpValid(VALID_TOTP_KEY, invalidOtp));
    }

}
