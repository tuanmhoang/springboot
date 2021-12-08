package com.tuanmhoang.springboot.uploads3.exception;

public class InvalidFileTypeException extends IllegalArgumentException {
    public InvalidFileTypeException(String message) {
        super(message);
    }
}
