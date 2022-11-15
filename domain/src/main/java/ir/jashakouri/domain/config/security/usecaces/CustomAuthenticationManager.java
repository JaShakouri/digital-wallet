package ir.jashakouri.domain.config.security.usecaces;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author jashakouri on 01.09.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Component
@AllArgsConstructor
@Slf4j(topic = "LOG_CustomAuthenticationManager")
public class CustomAuthenticationManager implements AuthenticationManager {

    private UserDetailsService customUserDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        log.info("username: {}", authentication.getPrincipal());
        final UserDetails userDetail = customUserDetailsService.loadUserByUsername(String.valueOf(authentication.getPrincipal()));

        if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetail.getPassword()))
            throw new BadCredentialsException("Wrong username or password");

        return new UsernamePasswordAuthenticationToken(
                userDetail.getUsername(), userDetail, userDetail.getAuthorities());
    }

}
