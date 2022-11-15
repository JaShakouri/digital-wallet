package ir.jashakouri.domain.exception.s3;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class S3BaseException extends Exception {
    public S3BaseException() {
        super("Can't access to s3 bucket");
    }

    public S3BaseException(String message) {
        super(message);
    }

    public S3BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public S3BaseException(Throwable cause) {
        super(cause);
    }

    protected S3BaseException(String message,
                              Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
