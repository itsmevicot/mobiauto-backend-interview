package com.mobiauto.backend.domain.exceptions.Permissao;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class PermissaoNotFoundException extends CustomException {
    public PermissaoNotFoundException() {
        super("Essa permissão não existe.", HttpStatus.valueOf(404));
    }
}
