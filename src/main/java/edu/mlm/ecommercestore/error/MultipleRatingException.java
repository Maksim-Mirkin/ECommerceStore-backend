package edu.mlm.ecommercestore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MultipleRatingException extends ECommerceException{

    public MultipleRatingException(String message) {
        super(message);
    }

    public MultipleRatingException(){
        super("You can't rate multiple times the same product!");
    }
}
