package com.bncrypted.authenticator.configuration;

import com.bncrypted.authenticator.model.UserTokenDetails;
import com.bncrypted.authenticator.util.jwt.JwtHS512Helper;
import com.bncrypted.authenticator.util.jwt.JwtHelper;
import com.bncrypted.authenticator.util.otp.OtpHelper;
import com.bncrypted.authenticator.util.otp.TotpHelper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OtpHelper getOtpHelper() {
        return new TotpHelper();
    }

    @Bean
    @ConfigurationProperties("authenticator.api.jwt")
    public JwtHelper getJwtHelper() {
        return new JwtHS512Helper<UserTokenDetails>();
    }

}
