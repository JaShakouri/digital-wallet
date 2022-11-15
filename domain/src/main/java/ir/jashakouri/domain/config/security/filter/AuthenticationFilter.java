package ir.jashakouri.domain.config.security.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.jashakouri.data.dto.response.BaseJwt;
import ir.jashakouri.data.dto.response.BaseResponse;
import ir.jashakouri.data.enums.TokenType;
import ir.jashakouri.domain.config.security.bruteForce.AttemptService;
import ir.jashakouri.domain.exception.user.UserLockedException;
import ir.jashakouri.data.utils.JWTGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ir.jashakouri.data.utils.NetworkUtils.getIp;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author jashakouri on 25.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Slf4j(topic = "LOG_AuthenticationManager")
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AttemptService attemptService;
    private final AuthenticationManager authenticationManager;
    private final Algorithm algorithm;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                Algorithm algorithm, AttemptService attemptService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.algorithm = algorithm;
        this.attemptService = attemptService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        log.info("call api ip: {}", getIp(request));

        if (attemptService.isBlocked(request))
            throw new UserLockedException("Account locked until few minutes, please try again after 5 minutes");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        User user = (User) authResult.getCredentials();

        var access_token = JWTGenerator.generate(request,
                passwordEncoder,
                user,
                this.algorithm,
                TokenType.ACCESS);

        var refresh_token = JWTGenerator.generate(request,
                passwordEncoder,
                user,
                this.algorithm,
                TokenType.REFRESH);

        var base = new BaseJwt(access_token, refresh_token);

        BaseResponse res = new BaseResponse();
        res.setStatus(HttpStatus.OK.getReasonPhrase());
        res.setStatusCode(HttpStatus.OK.value());
        res.setBody(base.getJWTMapModel());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), res);

        attemptService.successDetected(request);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        BaseResponse res = new BaseResponse();
        res.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setError(failed.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        new ObjectMapper().writeValue(response.getOutputStream(), res);

        attemptService.failedDetected(request);

    }

}
