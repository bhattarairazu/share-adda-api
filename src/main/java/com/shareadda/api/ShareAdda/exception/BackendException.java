package com.shareadda.api.ShareAdda.exception;

public class BackendException extends RuntimeException {

    public BackendException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BackendException(String s) {
        super(s);
    }
}
