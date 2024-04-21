package edu.mlm.ecommercestore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends ECommerceException {
    public AuthenticationException() {
        super("Not Authenticated");
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
