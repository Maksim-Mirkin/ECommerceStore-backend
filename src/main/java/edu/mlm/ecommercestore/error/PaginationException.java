package edu.mlm.ecommercestore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PaginationException extends ECommerceException {
    public PaginationException(String message){
        super(message);
    }
}
