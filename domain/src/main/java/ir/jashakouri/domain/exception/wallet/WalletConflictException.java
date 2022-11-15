package ir.jashakouri.domain.exception.wallet;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class WalletConflictException extends Exception {
    public WalletConflictException() {
        super("Wallet conflict");
    }

    public WalletConflictException(String message) {
        super(message);
    }

    public WalletConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletConflictException(Throwable cause) {
        super(cause);
    }

    protected WalletConflictException(String message,
                                      Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
