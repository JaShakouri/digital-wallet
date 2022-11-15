package ir.jashakouri.domain.exception.transaction;

import ir.jashakouri.domain.exception.s3.S3BaseException;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class TransactionDepositException extends S3BaseException {
    public TransactionDepositException() {
        super("Deposit Can't authenticate");
    }

    public TransactionDepositException(String message) {
        super(message);
    }

    public TransactionDepositException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionDepositException(Throwable cause) {
        super(cause);
    }

    protected TransactionDepositException(String message,
                                          Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
