package edu.mlm.ecommercestore.error;

import edu.mlm.ecommercestore.dto.exception.ExceptionDTO;
import edu.mlm.ecommercestore.dto.exception.InternalServerExceptionDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the e-commerce store application,
 * handling exceptions across the whole application.
 */
@ControllerAdvice
public class ECommerceExceptionHandler {

    private final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    private final HttpStatus forbidden = HttpStatus.FORBIDDEN;
    private final HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     * Handles custom {@link ECommerceException} which may not be annotated with {@link ResponseStatus}.
     *
     * @param e       the {@link ECommerceException} to handle.
     * @param method  the {@link HandlerMethod} in which the exception was thrown.
     * @param request the {@link HttpServletRequest} in which the exception occurred.
     * @return the {@link ExceptionDTO} and the HTTP status code.
     */
    @ExceptionHandler(ECommerceException.class)
    public ResponseEntity<ExceptionDTO> handleECommerceException(
            ECommerceException e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        if (!e.getClass().isAnnotationPresent(ResponseStatus.class)) {
            return ResponseEntity.internalServerError()
                    .body(getExceptionMessage(e, method, request, internalServerError));
        }
        val code = getHttpStatus(e);
        val dto = getExceptionMessage(e, method, request, code);
        return new ResponseEntity<>(dto, code);
    }

    /**
     * Handles {@link SQLIntegrityConstraintViolationException} and maps it to a bad request HTTP status.
     *
     * @param exc     the {@link SQLIntegrityConstraintViolationException} to handle.
     * @param method  the {@link HandlerMethod} in which the exception was thrown.
     * @param request the {@link HttpServletRequest} in which the exception occurred.
     * @return the {@link ExceptionDTO} and HTTP status code BAD_REQUEST.
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException exc,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(exc, method, request, badRequest);
        return ResponseEntity.badRequest().body(dto);
    }

    /**
     * Handles {@link MethodArgumentNotValidException} by returning detailed validation error messages.
     *
     * @param e       the {@link MethodArgumentNotValidException} to handle.
     * @param method  the {@link HandlerMethod} in which the exception was thrown.
     * @param request the {@link HttpServletRequest} in which the exception occurred.
     * @return a {@link ResponseEntity} containing a map of field errors and the BAD_REQUEST status.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(e, method, request, badRequest);
        var map = new HashMap<>(dto.dtoToMap());
        e.getBindingResult().getFieldErrors().forEach(
                error -> map.put("Field_" + error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(map);
    }

    /**
     * Handles {@link AccessDeniedException}, typically thrown when an authentication or authorization failure occurs.
     *
     * @param e       the {@link AccessDeniedException} to handle.
     * @param method  the {@link HandlerMethod} in which the exception was thrown.
     * @param request the {@link HttpServletRequest} in which the exception occurred.
     * @return the {@link ExceptionDTO} and the FORBIDDEN status.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDTO> handleAccessDeniedException(
            AccessDeniedException e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(e, method, request, forbidden);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
    }

    /**
     * Handles {@link MethodArgumentTypeMismatchException}, which is thrown when a method parameter is not the expected type.
     *
     * @param e       the {@link MethodArgumentTypeMismatchException} to handle.
     * @param method  the {@link HandlerMethod} in which the exception was thrown.
     * @param request the {@link HttpServletRequest} in which the exception occurred.
     * @return the {@link ExceptionDTO} and the BAD_REQUEST status.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(e, method, request, badRequest);
        return ResponseEntity.badRequest().body(dto);
    }

    /**
     * Handles {@link IllegalArgumentException} thrown by any controller method.
     * This handler captures the exception and constructs a detailed response based on the exception details.
     *
     * @param e       the {@link IllegalArgumentException} that was thrown.
     * @param method  the {@link HandlerMethod} where the exception was thrown. This provides context such as the specific controller and method invoked.
     * @param request the {@link HttpServletRequest} that resulted in the exception, which contains details like the HTTP request URL and parameters.
     * @return the {@link ExceptionDTO} and the BAD_REQUEST status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDTO> handleIllegalArgumentException(
            IllegalArgumentException e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(e, method, request, badRequest);
        return ResponseEntity.badRequest().body(dto);
    }

    /**
     * Handles generic Exceptions that are not handled by more specific handlers above.
     *
     * @param e       the {@link Exception} to handle.
     * @param method  the {@link HandlerMethod} in which the exception was thrown.
     * @param request the {@link HttpServletRequest} in which the exception occurred.
     * @return the {@link InternalServerExceptionDTO} and INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<InternalServerExceptionDTO> handleGenericException(
            Exception e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        InternalServerExceptionDTO dto = getInternalServerExceptionMessage(e, method, request);
        dto.setException(e.getClass().getSimpleName());
        return ResponseEntity.internalServerError().body(dto);
    }

    /**
     * Determines the HTTP status from the {@link ResponseStatus} annotation of an exception.
     *
     * @param e the {@link Exception} with the {@link ResponseStatus} annotation.
     * @return the HTTP status defined in the {@link ResponseStatus} annotation.
     */
    private HttpStatus getHttpStatus(Exception e) {
        val annotation = e.getClass().getAnnotation(ResponseStatus.class);
        return annotation.value();
    }

    /**
     * Creates an {@link ExceptionDTO} for logging and response purposes.
     *
     * @param e       the {@link Exception} to log.
     * @param method  the {@link HandlerMethod} in which the exception occurred.
     * @param request the {@link HttpServletRequest} in which the exception occurred.
     * @param status  the HTTP status associated with the exception.
     * @return an {@link ExceptionDTO} representing the exception details.
     */
    private ExceptionDTO getExceptionMessage(
            Exception e,
            HandlerMethod method,
            HttpServletRequest request,
            HttpStatus status
    ) {
        val controller = method.getMethod().getDeclaringClass().getSimpleName();
        val methodName = method.getMethod().getName();
        val httpMethod = request.getMethod();
        val path = request.getRequestURI();
        return ExceptionDTO.builder()
                .controller(controller)
                .controllerMethod(methodName)
                .method(httpMethod)
                .path(path)
                .message(e.getMessage())
                .status(status.value())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    /**
     * Creates an {@link InternalServerExceptionDTO} for logging and response purposes in case of general exceptions.
     *
     * @param e       the {@link Exception} to log.
     * @param method  the {@link HandlerMethod} in which the exception occurred.
     * @param request the {@link HttpServletRequest} in which the exception occurred.
     * @return an {@link InternalServerExceptionDTO} representing the detailed exception info.
     */
    private InternalServerExceptionDTO getInternalServerExceptionMessage(
            Exception e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val controller = method.getMethod().getDeclaringClass().getSimpleName();
        val methodName = method.getMethod().getName();
        val httpMethod = request.getMethod();
        val path = request.getRequestURI();
        return new InternalServerExceptionDTO(
                controller,
                methodName,
                httpMethod,
                path,
                e.getMessage(),
                LocalDateTime.now().toString(),
                e.getClass().getSimpleName()
        );
    }
}
