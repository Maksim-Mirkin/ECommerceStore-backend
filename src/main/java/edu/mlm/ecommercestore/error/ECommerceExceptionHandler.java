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

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ECommerceExceptionHandler {

    private final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    private final HttpStatus forbidden = HttpStatus.FORBIDDEN;
    private final HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

    @ExceptionHandler(ECommerceException.class)
    public ResponseEntity<ExceptionDTO> handleECommerceException(
            ECommerceException e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        //if exception doesn't have the response status annotation: return 500
        if (!e.getClass().isAnnotationPresent(ResponseStatus.class)) {
            return ResponseEntity.internalServerError()
                    .body(getExceptionMessage(e, method, request, internalServerError));
        }

        //else take the code from the annotation:
        val code = getHttpStatus(e);
        val dto = getExceptionMessage(e, method, request, code);
        return new ResponseEntity<>(dto, code);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handle(
            SQLIntegrityConstraintViolationException exc,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(exc, method, request, badRequest);
        return ResponseEntity.badRequest().body(dto);
    }

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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDTO> handle(
            AccessDeniedException e,
            HandlerMethod method,
            HttpServletRequest request
    ) {
        val dto = getExceptionMessage(e, method, request, forbidden);
        return ResponseEntity.status(forbidden).body(dto);
    }

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

    private HttpStatus getHttpStatus(Exception e) {
        val annotation = e.getClass().getAnnotation(ResponseStatus.class);
        return annotation.value();
    }

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
