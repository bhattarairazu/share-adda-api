package com.shareadda.api.ShareAdda.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ForbiddenException(String s) {
        super(s);
    }
}
