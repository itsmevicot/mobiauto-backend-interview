package com.mobiauto.backend.domain.exceptions.Cliente;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends CustomException {
    public EmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
