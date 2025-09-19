package com.supplychain.exceptions;

public class SearchBuildersException extends RuntimeException {

    private static final long serialVersionUID = 600986566875150534L;

    public SearchBuildersException() {
    }

    public SearchBuildersException(String message) {
	super(message);
    }

    public SearchBuildersException(String message, Throwable cause) {
	super(message, cause);
    }

    public SearchBuildersException(Throwable cause) {
	super(cause);
    }

    public SearchBuildersException(String message, Throwable cause, boolean enableSuppression,
	    boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }
}
