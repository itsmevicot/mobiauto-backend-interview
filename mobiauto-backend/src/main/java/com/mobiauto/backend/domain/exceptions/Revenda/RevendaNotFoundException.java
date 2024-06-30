package com.mobiauto.backend.domain.exceptions.Revenda;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class RevendaNotFoundException extends CustomException {
    public RevendaNotFoundException() {
        super("Essa revenda não existe.", HttpStatus.valueOf(404));
    }
}
