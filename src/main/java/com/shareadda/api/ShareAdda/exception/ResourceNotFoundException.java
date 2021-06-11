package com.shareadda.api.ShareAdda.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String s) {
        super(s);
    }

    public ResourceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
