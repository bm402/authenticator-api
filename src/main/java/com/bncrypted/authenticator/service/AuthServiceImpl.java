package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.exception.InvalidCredentialsException;
import com.bncrypted.authenticator.model.TokenCredentials;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserCredentials;
import com.bncrypted.authenticator.model.UserResponse;
import com.bncrypted.authenticator.repository.AuthDao;
import com.bncrypted.authenticator.util.jwt.JwtHelper;
import com.bncrypted.authenticator.util.otp.OtpHelper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class AuthServiceImpl implements AuthService {

    private final Jdbi jdbi;
    private final PasswordEncoder passwordEncoder;
    private final OtpHelper otpHelper;
    private final JwtHelper jwtHelper;

    public AuthServiceImpl(DataSource dataSource,
                           PasswordEncoder passwordEncoder,
                           OtpHelper otpHelper,
                           JwtHelper jwtHelper) {

        this.jdbi = Jdbi.create(dataSource)
                .installPlugin(new SqlObjectPlugin());
        this.passwordEncoder = passwordEncoder;
        this.otpHelper = otpHelper;
        this.jwtHelper = jwtHelper;
    }

    public TokenCredentials lease(UserAndOtp userAndOtp) {
        UserCredentials storedCredentials = jdbi.withExtension(AuthDao.class,
                dao -> dao.getUserCredentials(userAndOtp.getUsername()));

        if (!passwordEncoder.matches(userAndOtp.getPassword(), storedCredentials.getHashedPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!otpHelper.isOtpValid(storedCredentials.getMfaKey(), userAndOtp.getOneTimePassword())) {
            throw new InvalidCredentialsException("Invalid one-time password");
        }

        return new TokenCredentials(jwtHelper.issueTokenForUser(userAndOtp.getUsername()));
    }

    public TokenCredentials leaseGuest() {
        return new TokenCredentials(jwtHelper.issueTokenForUser("guest"));
    }

    public UserResponse verify(TokenCredentials tokenCredentials) {
        String extractedUsername = jwtHelper.verifyAndExtractUser(tokenCredentials.getToken());
        return new UserResponse(extractedUsername, "Verification successful");
    }

}
