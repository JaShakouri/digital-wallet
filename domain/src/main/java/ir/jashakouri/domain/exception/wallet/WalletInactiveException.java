package ir.jashakouri.domain.exception.wallet;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class WalletInactiveException extends Exception {
    public WalletInactiveException() {
        super("Wallet is inactive");
    }

    public WalletInactiveException(String message) {
        super(message);
    }

    public WalletInactiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletInactiveException(Throwable cause) {
        super(cause);
    }

    protected WalletInactiveException(String message,
                                      Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
