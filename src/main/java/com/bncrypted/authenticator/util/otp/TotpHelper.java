package com.bncrypted.authenticator.util.otp;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.IGoogleAuthenticator;

public class TotpHelper implements OtpHelper {

    private final IGoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

    public String issueOtp(String base32EncodedKey) {
        return String.valueOf(googleAuthenticator.getTotpPassword(base32EncodedKey));
    }

    public boolean isOtpValid(String base32EncodedKey, String token) {
        return googleAuthenticator.authorize(base32EncodedKey, Integer.parseInt(token));
    }

}
