package ir.jashakouri.domain.config.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import ir.jashakouri.data.dto.response.BaseResponse;
import ir.jashakouri.data.utils.CommonVariables;
import ir.jashakouri.data.utils.NetworkUtils;
import ir.jashakouri.domain.config.security.bruteForce.AttemptService;
import ir.jashakouri.domain.exception.user.UserLockedException;
import ir.jashakouri.domain.services.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author jashakouri on 25.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Slf4j(topic = "LOG_AuthorizationManager")
public class AuthorizationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final AttemptService attemptService;
    private final Algorithm algorithm;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationFilter(AttemptService attemptService, Algorithm algorithm,
                               UserService userService, PasswordEncoder passwordEncoder) {
        this.attemptService = attemptService;
        this.algorithm = algorithm;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws IOException, UserLockedException {

        try {

            if (attemptService.isBlocked(request))
                throw new UserLockedException("Account locked until few minutes, please try again after 5 minutes");

            String bearer_ = "Bearer ";
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(bearer_)) {

                if (CommonVariables.WHITELIST.stream().anyMatch(request.getServletPath()::contains)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                forbiddenException(request, response, new IllegalAccessException("You don't have access"));
                return;
            }

            String token = authorizationHeader.replace(bearer_, "");

            JWTVerifier jwtVerifier = JWT.require(this.algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String username = decodedJWT.getSubject();
            var userChecker = userService.getUser(username);
            if (userChecker.isEmpty()) {
                forbiddenException(request, response, new IllegalAccessException("User deactivate or my be deleted"));
                return;
            }

            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            String sessionId = decodedJWT.getClaim("sessionId").asString();

            if (sessionId != null && passwordEncoder.matches(NetworkUtils.getIp(request), sessionId)) {

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);

                attemptService.successDetected(request);

            } else {
                forbiddenException(request, response, new IllegalAccessException("Token is missing\nPlease get a new token"));
            }

        } catch (Exception ex) {
            forbiddenException(request, response, ex);
        }

    }

    private void forbiddenException(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response, Exception ex) throws IOException {
        BaseResponse res = new BaseResponse();
        res.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setError(ex.getMessage());

        response.setHeader("error", ex.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        new ObjectMapper().writeValue(response.getOutputStream(), res);

        attemptService.failedDetected(request);
    }

}
