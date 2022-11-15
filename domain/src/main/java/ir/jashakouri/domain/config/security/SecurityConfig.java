package ir.jashakouri.domain.config.security;

import com.auth0.jwt.algorithms.Algorithm;
import ir.jashakouri.data.utils.CommonVariables;
import ir.jashakouri.domain.config.security.bruteForce.AttemptService;
import ir.jashakouri.domain.config.security.filter.AuthenticationFilter;
import ir.jashakouri.domain.config.security.filter.AuthorizationFilter;
import ir.jashakouri.domain.config.security.usecaces.CustomAuthenticationManager;
import ir.jashakouri.domain.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author jashakouri on 25.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j(topic = "LOG_SecurityConfig")
public class SecurityConfig {

    private final UserService userService;
    private final CustomAuthenticationManager authenticationManager;
    private final AttemptService attemptService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager, algorithm(), attemptService, passwordEncoder);
        authenticationFilter.setFilterProcessesUrl("/api/auth/login");

        AuthorizationFilter authorizationFilter = new AuthorizationFilter(attemptService,
                algorithm(), userService, passwordEncoder);

        http
                .csrf().disable().httpBasic().and()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .userDetailsService(userService)
                .addFilter(authenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(CommonVariables.AUTH_WHITELIST).permitAll()
                .antMatchers(CommonVariables.SOURCE_WHITELIST).permitAll()
                .anyRequest()
                .authenticated();

        return http.build();
    }

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(CommonVariables.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

}
