package ir.jashakouri.data.validator.user;

import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;

/**
 * @author jashakouri on 28.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class CustomUserDetailsChecker implements UserDetailsChecker {

    @Override
    public void check(UserDetails user) {

        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }

        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }

    }

}
