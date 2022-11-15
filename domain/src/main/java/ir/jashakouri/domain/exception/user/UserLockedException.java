package ir.jashakouri.domain.exception.user;

import org.springframework.security.core.AuthenticationException;

/**
 * @author jashakouri on 31.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class UserLockedException extends AuthenticationException {

    public UserLockedException(String msg) {
        super(msg);
    }

    public UserLockedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
