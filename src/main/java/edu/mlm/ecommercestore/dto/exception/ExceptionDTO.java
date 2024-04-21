package edu.mlm.ecommercestore.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Data Transfer Object for conveying exception details in responses.
 * This DTO includes detailed information about where and why an exception occurred within the application,
 * facilitating debugging and error handling.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for conveying exception details in responses.")
public class ExceptionDTO {

    /**
     * The controller class where the exception occurred.
     */
    @Schema(description = "The controller class where the exception occurred.",
            example = "ProductController")
    private String controller;

    /**
     * The controller method where the exception occurred.
     */
    @Schema(description = "The controller method where the exception occurred.",
            example = "getProductById")
    private String controllerMethod;

    /**
     * The HTTP method of the request that caused the exception.
     */
    @Schema(description = "The HTTP method of the request that caused the exception.", example = "GET")
    private String method;

    /**
     * The path of the request that caused the exception.
     */
    @Schema(description = "The path of the request that caused the exception.",
            example = "/api/v1/products/{id}")
    private String path;

    /**
     * A message detailing the exception.
     */
    @Schema(description = "A message detailing the exception.",
            example = "Entity Product with id = 123 not found!")
    private String message;

    /**
     * The HTTP status code of the response.
     */
    @Schema(description = "The HTTP status code of the response.",
            example = "404")
    private int status;

    /**
     * The timestamp when the exception occurred.
     */
    @Schema(description = "The timestamp when the exception occurred.",
            example = "2023-01-01T12:00:00.0000000")
    private String timestamp;

    /**
     * Converts DTO fields into a map for easy serialization and response formatting.
     *
     * @return A map representation of the DTO.
     */
    public Map<String, Object> dtoToMap() {
        return Map.of(
                "controller", controller,
                "controllerMethod", controllerMethod,
                "method", method,
                "path", path,
                "message", message,
                "status", String.valueOf(status),
                "timestamp", timestamp
        );
    }
}
