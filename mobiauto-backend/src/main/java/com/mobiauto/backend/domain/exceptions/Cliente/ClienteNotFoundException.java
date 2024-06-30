package com.mobiauto.backend.domain.exceptions.Cliente;

import com.mobiauto.backend.domain.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class ClienteNotFoundException extends CustomException {
    public ClienteNotFoundException() {
        super("Cliente não encontrado!", HttpStatus.valueOf(404));
    }
}
