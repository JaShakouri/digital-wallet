package ir.jashakouri.domain.exception.wallet;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class WalletRequirementFileException extends Exception {
    public WalletRequirementFileException() {
        super("Not found invoice file");
    }

    public WalletRequirementFileException(String message) {
        super(message);
    }

    public WalletRequirementFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletRequirementFileException(Throwable cause) {
        super(cause);
    }

    protected WalletRequirementFileException(String message,
                                             Throwable cause,
                                             boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
