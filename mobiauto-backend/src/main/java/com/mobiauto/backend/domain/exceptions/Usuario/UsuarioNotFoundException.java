package com.mobiauto.backend.domain.exceptions.Usuario;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class UsuarioNotFoundException extends CustomException {
    public UsuarioNotFoundException() {
        super("Usuário não encontrado!", HttpStatus.valueOf(404));
    }
}
