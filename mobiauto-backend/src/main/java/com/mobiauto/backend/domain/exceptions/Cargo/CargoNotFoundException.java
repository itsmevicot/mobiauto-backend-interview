package com.mobiauto.backend.domain.exceptions.Cargo;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class CargoNotFoundException extends CustomException {
    public CargoNotFoundException() {
        super("Cargo não encontrado!", HttpStatus.valueOf(404));
    }
}
