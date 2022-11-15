package ir.jashakouri.domain.exception.wallet;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class WalletNotFoundException extends Exception {
    public WalletNotFoundException() {
        super("Wallet not found");
    }

    public WalletNotFoundException(String message) {
        super(message);
    }

    public WalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletNotFoundException(Throwable cause) {
        super(cause);
    }

    protected WalletNotFoundException(String message,
                                      Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
