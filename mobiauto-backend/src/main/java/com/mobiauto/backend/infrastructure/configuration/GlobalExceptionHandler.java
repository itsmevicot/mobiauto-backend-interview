package com.mobiauto.backend.infrastructure.configuration;

import com.mobiauto.backend.domain.exceptions.CustomException;
import com.mobiauto.backend.domain.models.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RuntimeException.class, CustomException.class})
    public ResponseEntity<HttpResponse> handleRuntimeException(RuntimeException exception) {
        exception.printStackTrace();
        if (exception instanceof CustomException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(HttpResponse.builder()
                            .timestamp(LocalDateTime.now().toString())
                            .statusCode(ex.getStatus().value())
                            .status(ex.getStatus())
                            .message(ex.getMessage())
                            .build()
                    );
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        HttpResponse.builder()
                                .timestamp(LocalDateTime.now().toString())
                                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .message("An error occurred: " + exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<HttpResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        HttpResponse.builder()
                                .timestamp(LocalDateTime.now().toString())
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .status(HttpStatus.BAD_REQUEST)
                                .message("Validation failed")
                                .data(Map.of("errors", errors))
                                .build()
                );
    }
}
