package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.exception.InvalidCredentialsException;
import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserCredentials;
import com.bncrypted.authenticator.model.UserResponse;
import com.bncrypted.authenticator.repository.AuthDao;
import com.bncrypted.authenticator.util.jwt.JwtHS512Helper;
import com.bncrypted.authenticator.util.jwt.JwtHelper;
import com.bncrypted.authenticator.util.otp.OtpHelper;
import com.bncrypted.authenticator.util.otp.TotpHelper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class AuthServiceImpl implements AuthService {

    private final Jdbi jdbi;
    private final PasswordEncoder passwordEncoder;

    private final JwtHelper jwtHelper;
    private final OtpHelper otpHelper;

    public AuthServiceImpl(DataSource dataSource,
                           PasswordEncoder passwordEncoder,
                           @Value("${jwt.key}") String jwtKey,
                           @Value("${jwt.ttl}") int ttlInSeconds) {

        this.jdbi = Jdbi.create(dataSource)
                .installPlugin(new SqlObjectPlugin());
        this.passwordEncoder = passwordEncoder;

        this.jwtHelper = new JwtHS512Helper(jwtKey, ttlInSeconds);
        this.otpHelper = new TotpHelper();
    }

    public Token lease(UserAndOtp userAndOtp) {
        UserCredentials storedCredentials = jdbi.withExtension(AuthDao.class,
                dao -> dao.getUserCredentials(userAndOtp.getUsername()));

        if (!passwordEncoder.matches(userAndOtp.getPassword(), storedCredentials.getHashedPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!otpHelper.isOtpValid(storedCredentials.getMfaKey(), userAndOtp.getOneTimePassword())) {
            throw new InvalidCredentialsException("Invalid one-time password");
        }

        return new Token(jwtHelper.issueTokenForUser(userAndOtp.getUsername()));
    }

    public Token leaseGuest() {
        return new Token(jwtHelper.issueTokenForUser("guest"));
    }

    public UserResponse verify(Token token) {
        return new UserResponse("guest-user", "Verification successful");
    }

}
