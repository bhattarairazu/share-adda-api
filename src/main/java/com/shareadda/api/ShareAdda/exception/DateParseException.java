package com.shareadda.api.ShareAdda.exception;

public class DateParseException extends RuntimeException {
    public DateParseException(String s) {
        super(s);
    }

    public DateParseException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
