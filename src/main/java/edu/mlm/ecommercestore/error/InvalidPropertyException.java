package edu.mlm.ecommercestore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPropertyException extends ECommerceException {

    public InvalidPropertyException(String property) {
        super("Invalid property: " + property);
    }
}
