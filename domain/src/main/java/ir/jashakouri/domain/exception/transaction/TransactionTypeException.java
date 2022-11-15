package ir.jashakouri.domain.exception.transaction;

import ir.jashakouri.domain.exception.s3.S3BaseException;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class TransactionTypeException extends S3BaseException {
    public TransactionTypeException() {
        super("Transaction is invalid");
    }

    public TransactionTypeException(String message) {
        super(message);
    }

    public TransactionTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionTypeException(Throwable cause) {
        super(cause);
    }

    protected TransactionTypeException(String message,
                                       Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
