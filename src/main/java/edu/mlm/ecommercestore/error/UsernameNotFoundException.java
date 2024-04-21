package edu.mlm.ecommercestore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UsernameNotFoundException extends ECommerceException{

    public UsernameNotFoundException(String message) {
        super(message);
    }

    public UsernameNotFoundException(){
        super("Provided username is not exist");
    }

}
