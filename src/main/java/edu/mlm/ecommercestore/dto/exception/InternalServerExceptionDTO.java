package edu.mlm.ecommercestore.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * Data Transfer Object for internal server exceptions,
 * extending the generic ExceptionDTO with additional internal server error details.
 * It includes a default internal server error message and details about the specific exception.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Schema(description = "Data Transfer Object for internal server exceptions," +
        " extending the generic ExceptionDTO with additional internal server error details.")
public class InternalServerExceptionDTO extends ExceptionDTO {
    @Schema(description = "A message indicating an internal server error occurred," +
            " advising to contact the admin.")
    private final String internalServerError = "Contact Admin";
    @Schema(
            description = "Details about the exception that occurred," +
                    " potentially including stack trace information or a more detailed error message.",
            example = "NullPointerException"
    )
    private String exception;

    /**
     * Constructs a new InternalServerExceptionDTO with detailed information about the internal server exception.
     *
     * @param controller        The controller where the exception occurred.
     * @param controllerMethod  The method of the controller where the exception occurred.
     * @param method            The HTTP method during which the exception occurred.
     * @param path              The request path that led to the exception.
     * @param message           The error message associated with the exception.
     * @param timestamp         The timestamp when the exception was logged.
     * @param exception         Specific details about the exception.
     */
    public InternalServerExceptionDTO(
            String controller,
            String controllerMethod,
            String method,
            String path,
            String message,
            String timestamp,
            String exception
    ) {
        super(
                controller,
                controllerMethod,
                method,
                path,
                message,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                timestamp
        );
        this.exception = exception;
    }
}
