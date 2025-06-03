package com.appointment.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

//import com.example.demo.exception.GlobalExceptionHandler;

import org.slf4j.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
         logger.error("An error occurred: {}", ex.getMessage(), ex);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                 .body("An unexpected error occurred: " + ex.getMessage());
    }
}
