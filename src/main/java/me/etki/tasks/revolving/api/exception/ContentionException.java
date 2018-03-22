package me.etki.tasks.revolving.api.exception;

public class ContentionException extends Exception {
    public ContentionException() {
        super();
    }

    public ContentionException(String message) {
        super(message);
    }

    public ContentionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentionException(Throwable cause) {
        super(cause);
    }

    protected ContentionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
