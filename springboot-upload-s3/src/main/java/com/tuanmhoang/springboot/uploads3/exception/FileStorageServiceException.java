package com.tuanmhoang.springboot.uploads3.exception;

public class FileStorageServiceException extends RuntimeException {
    public FileStorageServiceException(String message, Exception e) {
        super(message, e);
    }
}
