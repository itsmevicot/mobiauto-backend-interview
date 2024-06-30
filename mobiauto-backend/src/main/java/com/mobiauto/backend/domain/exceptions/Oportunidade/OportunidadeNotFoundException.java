package com.mobiauto.backend.domain.exceptions.Oportunidade;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class OportunidadeNotFoundException extends CustomException {
    public OportunidadeNotFoundException() {
        super("Oportunidade não encontrado!", HttpStatus.valueOf(404));
    }
}
