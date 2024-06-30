package com.mobiauto.backend.domain.exceptions.Usuario;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends CustomException {
    public EmailAlreadyExistsException() {
        super("Já existe um usuário com esse e-mail!", HttpStatus.valueOf(409));
    }
}
