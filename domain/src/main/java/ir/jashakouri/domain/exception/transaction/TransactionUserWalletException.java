package ir.jashakouri.domain.exception.transaction;

import ir.jashakouri.domain.exception.s3.S3BaseException;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class TransactionUserWalletException extends S3BaseException {
    public TransactionUserWalletException() {
        super("User or wallet can't be null");
    }

    public TransactionUserWalletException(String message) {
        super(message);
    }

    public TransactionUserWalletException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionUserWalletException(Throwable cause) {
        super(cause);
    }

    protected TransactionUserWalletException(String message,
                                             Throwable cause,
                                             boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
