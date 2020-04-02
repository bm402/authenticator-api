package com.bncrypted.authenticator.util.otp;

public interface OtpHelper {

    String issueOtp(String base32EncodedKey);
    boolean isOtpValid(String base32EncodedKey, String token);

}
