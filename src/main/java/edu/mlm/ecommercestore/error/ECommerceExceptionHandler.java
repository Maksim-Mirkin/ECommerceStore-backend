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
     * Handles custom ECommerceException which may not be annotated with @ResponseStatus.
     *
     * @param e       the ECommerceException to handle.
     * @param method  the method in which the exception was thrown.
     * @param request the HttpServletRequest in which the exception occurred.
     * @return a ResponseEntity containing the ExceptionDTO and the HTTP status code.
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
     * Handles SQLIntegrityConstraintViolationException and maps it to a bad request HTTP status.
     *
     * @param exc     the SQLIntegrityConstraintViolationException to handle.
     * @param method  the method in which the exception was thrown.
     * @param request the HttpServletRequest in which the exception occurred.
     * @return a ResponseEntity containing the ExceptionDTO and HTTP status code BAD_REQUEST.
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handle(
            SQLIntegrityConstraintViolationException exc,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(exc, method, request, badRequest);
        return ResponseEntity.badRequest().body(dto);
    }

    /**
     * Handles MethodArgumentNotValidException by returning detailed validation error messages.
     *
     * @param e       the MethodArgumentNotValidException to handle.
     * @param method  the method in which the exception was thrown.
     * @param request the HttpServletRequest in which the exception occurred.
     * @return a ResponseEntity containing a map of field errors and the BAD_REQUEST status.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handle(
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
     * Handles AccessDeniedException, typically thrown when an authentication or authorization failure occurs.
     *
     * @param e       the AccessDeniedException to handle.
     * @param method  the method in which the exception was thrown.
     * @param request the HttpServletRequest in which the exception occurred.
     * @return a ResponseEntity containing the ExceptionDTO and the FORBIDDEN status.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDTO> handle(
            AccessDeniedException e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(e, method, request, forbidden);
        return ResponseEntity.status(forbidden).body(dto);
    }

    /**
     * Handles MethodArgumentTypeMismatchException, which is thrown when a method parameter is not the expected type.
     *
     * @param e       the MethodArgumentTypeMismatchException to handle.
     * @param method  the method in which the exception was thrown.
     * @param request the HttpServletRequest in which the exception occurred.
     * @return a ResponseEntity containing the ExceptionDTO and the BAD_REQUEST status.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> handle(
            MethodArgumentTypeMismatchException e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(e, method, request, badRequest);
        return ResponseEntity.badRequest().body(dto);
    }

    /**
     * Handles generic Exceptions that are not handled by more specific handlers above.
     *
     * @param e       the Exception to handle.
     * @param method  the method in which the exception was thrown.
     * @param request the HttpServletRequest in which the exception occurred.
     * @return a ResponseEntity containing the InternalServerExceptionDTO and INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<InternalServerExceptionDTO> handle(
            Exception e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        InternalServerExceptionDTO dto = getInternalServerExceptionMessage(e, method, request);
        dto.setException(e.getClass().getSimpleName());
        return ResponseEntity.internalServerError().body(dto);
    }

    /**
     * Determines the HTTP status from the ResponseStatus annotation of an exception.
     *
     * @param e the exception with the ResponseStatus annotation.
     * @return the HTTP status defined in the ResponseStatus annotation.
     */
    private HttpStatus getHttpStatus(Exception e) {
        val annotation = e.getClass().getAnnotation(ResponseStatus.class);
        return annotation.value();
    }

    /**
     * Creates an ExceptionDTO for logging and response purposes.
     *
     * @param e       the Exception to log.
     * @param method  the method in which the exception occurred.
     * @param request the HttpServletRequest in which the exception occurred.
     * @param status  the HTTP status associated with the exception.
     * @return an ExceptionDTO representing the exception details.
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
     * Creates an InternalServerExceptionDTO for logging and response purposes in case of general exceptions.
     *
     * @param e       the Exception to log.
     * @param method  the method in which the exception occurred.
     * @param request the HttpServletRequest in which the exception occurred.
     * @return an InternalServerExceptionDTO representing the detailed exception info.
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
