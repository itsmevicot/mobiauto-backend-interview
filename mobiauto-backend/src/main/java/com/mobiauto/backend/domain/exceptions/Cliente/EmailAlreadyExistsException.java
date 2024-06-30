package com.mobiauto.backend.domain.exceptions.Cliente;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends CustomException {
    public EmailAlreadyExistsException() {
        super("Esse e-mail já está cadastrado!", HttpStatus.valueOf(409));
    }
}
