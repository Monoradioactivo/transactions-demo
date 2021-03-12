package mx.simio.transactionsdemo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResourceNotFoundHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        ApiError apiError = new ApiError("Resource not found", notFound, notFound.value(), LocalDateTime.now(),
                e.getMessage());

        return new ResponseEntity<>(apiError, notFound);
    }
}
