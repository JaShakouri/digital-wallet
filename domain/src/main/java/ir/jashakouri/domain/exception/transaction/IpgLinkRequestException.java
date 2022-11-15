package ir.jashakouri.domain.exception.transaction;

import ir.jashakouri.domain.exception.s3.S3BaseException;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class IpgLinkRequestException extends S3BaseException {
    public IpgLinkRequestException() {
        super("Can't create payment link");
    }

    public IpgLinkRequestException(String message) {
        super(message);
    }

    public IpgLinkRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public IpgLinkRequestException(Throwable cause) {
        super(cause);
    }

    protected IpgLinkRequestException(String message,
                                      Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
