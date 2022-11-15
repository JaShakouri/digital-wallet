package ir.jashakouri.domain.exception.wallet;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class WalletBalanceException extends Exception {
    public WalletBalanceException() {
        super("Wallet have balance");
    }

    public WalletBalanceException(String message) {
        super(message);
    }

    public WalletBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletBalanceException(Throwable cause) {
        super(cause);
    }

    protected WalletBalanceException(String message,
                                     Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
