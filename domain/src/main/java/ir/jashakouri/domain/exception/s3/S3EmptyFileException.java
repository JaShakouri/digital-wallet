package ir.jashakouri.domain.exception.s3;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class S3EmptyFileException extends S3BaseException {
    public S3EmptyFileException() {
        super("File can't be empty");
    }

    public S3EmptyFileException(String message) {
        super(message);
    }

    public S3EmptyFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public S3EmptyFileException(Throwable cause) {
        super(cause);
    }

    protected S3EmptyFileException(String message,
                                   Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
