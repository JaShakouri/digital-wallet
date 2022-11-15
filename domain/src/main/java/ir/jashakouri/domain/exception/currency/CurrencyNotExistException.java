package ir.jashakouri.domain.exception.currency;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class CurrencyNotExistException extends Exception {
    public CurrencyNotExistException() {
        super("Currency not exists");
    }

    public CurrencyNotExistException(String message) {
        super(message);
    }

    public CurrencyNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyNotExistException(Throwable cause) {
        super(cause);
    }

    protected CurrencyNotExistException(String message,
                                        Throwable cause,
                                        boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
