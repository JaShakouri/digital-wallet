package ir.jashakouri.domain.exception.currency;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class CurrencyExistException extends Exception {
    public CurrencyExistException() {
        super("Currency name already taken");
    }

    public CurrencyExistException(String message) {
        super(message);
    }

    public CurrencyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyExistException(Throwable cause) {
        super(cause);
    }

    protected CurrencyExistException(String message,
                                     Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
