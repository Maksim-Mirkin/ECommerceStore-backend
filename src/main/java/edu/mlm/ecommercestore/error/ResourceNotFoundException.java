package edu.mlm.ecommercestore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ECommerceException{
    public ResourceNotFoundException(String entityName, String property, Object value){
        super("Entity %s with %s = %s not found!".formatted(entityName,property,value));
    }

    public static Supplier<ResourceNotFoundException> newInstance(String entityName, String property, Object value) {
        return () -> new ResourceNotFoundException(entityName,property,value);
    }
}
