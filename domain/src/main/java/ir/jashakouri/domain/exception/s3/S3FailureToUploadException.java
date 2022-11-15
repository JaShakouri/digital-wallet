package ir.jashakouri.domain.exception.s3;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class S3FailureToUploadException extends S3BaseException {
    public S3FailureToUploadException() {
        super("Upload file was failed");
    }

    public S3FailureToUploadException(String message) {
        super(message);
    }

    public S3FailureToUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public S3FailureToUploadException(Throwable cause) {
        super(cause);
    }

    protected S3FailureToUploadException(String message,
                                         Throwable cause,
                                         boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
