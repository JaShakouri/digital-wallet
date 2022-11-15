package ir.jashakouri.domain.exception.user;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class PhoneNumberExists extends Exception {
    public PhoneNumberExists() {
        super();
    }

    public PhoneNumberExists(String message) {
        super("Phone number " + message + " already taken");
    }

    public PhoneNumberExists(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNumberExists(Throwable cause) {
        super(cause);
    }

    protected PhoneNumberExists(String message, Throwable cause,
                                boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
