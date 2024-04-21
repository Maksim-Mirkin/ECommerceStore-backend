package edu.mlm.ecommercestore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidStatusException extends ECommerceException{
    public InvalidStatusException(String message) {
        super(message);
    }

    public InvalidStatusException(){
        super("Invalid status! Must be 'approved','declined' or 'pending'!");
    }
}
