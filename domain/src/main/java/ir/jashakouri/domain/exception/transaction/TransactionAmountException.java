package ir.jashakouri.domain.exception.transaction;

import ir.jashakouri.domain.exception.s3.S3BaseException;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class TransactionAmountException extends S3BaseException {
    public TransactionAmountException() {
        super("Amount can't be lower or equals zero");
    }

    public TransactionAmountException(String message) {
        super(message);
    }

    public TransactionAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionAmountException(Throwable cause) {
        super(cause);
    }

    protected TransactionAmountException(String message,
                                         Throwable cause,
                                         boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
