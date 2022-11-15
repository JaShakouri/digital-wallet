package ir.jashakouri.domain.exception.s3;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public class S3CreateFolderException extends S3BaseException {
    public S3CreateFolderException() {
        super("Can't create folder on s3 bucket");
    }

    public S3CreateFolderException(String message) {
        super(message);
    }

    public S3CreateFolderException(String message, Throwable cause) {
        super(message, cause);
    }

    public S3CreateFolderException(Throwable cause) {
        super(cause);
    }

    protected S3CreateFolderException(String message,
                                      Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
