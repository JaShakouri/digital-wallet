package ir.jashakouri.domain.exception.s3;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class S3MimeTypeException extends S3BaseException {
    public S3MimeTypeException() {
        super("Content type is not valid");
    }

    public S3MimeTypeException(String message) {
        super(message);
    }

    public S3MimeTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public S3MimeTypeException(Throwable cause) {
        super(cause);
    }

    protected S3MimeTypeException(String message,
                                  Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
