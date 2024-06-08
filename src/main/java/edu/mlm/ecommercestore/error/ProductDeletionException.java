package edu.mlm.ecommercestore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown when an attempt to delete a product fails
 * due to it being referenced by other database entities.
 * This exception is mapped to a BAD_REQUEST HTTP status code, indicating
 * that the request could not be processed because of a client error.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductDeletionException extends ECommerceException {
    public ProductDeletionException(String message) {
        super(message);
    }
}
