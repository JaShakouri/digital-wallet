package ir.jashakouri.domain.exception.transaction;

import ir.jashakouri.domain.exception.s3.S3BaseException;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class TransactionErrorException extends S3BaseException {
    public TransactionErrorException() {
        super("Transaction can't create");
    }

    public TransactionErrorException(String message) {
        super(message);
    }

    public TransactionErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionErrorException(Throwable cause) {
        super(cause);
    }

    protected TransactionErrorException(String message,
                                        Throwable cause,
                                        boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
