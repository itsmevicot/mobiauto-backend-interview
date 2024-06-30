package com.mobiauto.backend.domain.exceptions.Perfil;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class PerfilNotFoundException extends CustomException {
    public PerfilNotFoundException() {
        super("Esse perfil não existe.", HttpStatus.valueOf(404));
    }
}
