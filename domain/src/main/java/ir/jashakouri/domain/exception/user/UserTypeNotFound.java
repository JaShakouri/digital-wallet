package ir.jashakouri.domain.exception.user;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class UserTypeNotFound extends Exception {
    public UserTypeNotFound() {
        super("UserType doesn't exist");
    }

    public UserTypeNotFound(String message) {
        super(message);
    }

    public UserTypeNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTypeNotFound(Throwable cause) {
        super(cause);
    }

    protected UserTypeNotFound(String message, Throwable cause,
                               boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
