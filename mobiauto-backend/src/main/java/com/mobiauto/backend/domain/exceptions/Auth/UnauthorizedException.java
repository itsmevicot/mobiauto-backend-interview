package com.mobiauto.backend.domain.exceptions.Auth;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException() {
        super("Você não tem autorização para essa ação.", HttpStatus.valueOf(403));
    }
}
