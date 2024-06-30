package com.mobiauto.backend.domain.exceptions.Veiculo;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class VeiculoNotFoundException extends CustomException {
    public VeiculoNotFoundException() {
        super("Esse veículo não existe.", HttpStatus.valueOf(404));
    }
}
