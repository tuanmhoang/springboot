package com.tuanmhoang.springboot.uploads3.controller;

import com.tuanmhoang.springboot.uploads3.exception.FileStorageServiceException;
import com.tuanmhoang.springboot.uploads3.exception.InvalidFileTypeException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(InvalidFileTypeException.class)
    @ResponseBody
    public ResponseEntity handleInvalidFileType(IllegalArgumentException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                Map.of("error", exception.getMessage())
            );
    }

    @ExceptionHandler(FileStorageServiceException.class)
    @ResponseBody
    public ResponseEntity handleFileStorageServiceException(RuntimeException exception) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                Map.of("error", exception.getMessage())
            );
    }
}
