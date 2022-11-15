package ir.jashakouri.domain.exception.user;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class UsernameExists extends Exception {
    public UsernameExists() {
        super("Username already taken");
    }

    public UsernameExists(String message) {
        super("Username " + message + " already taken");
    }

    public UsernameExists(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameExists(Throwable cause) {
        super(cause);
    }

    protected UsernameExists(String message, Throwable cause,
                             boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
