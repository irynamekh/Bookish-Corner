package com.bookstore.exception;

public class InvalidUpdateDataException extends RuntimeException {
    public InvalidUpdateDataException(String message) {
        super(message);
    }
}
