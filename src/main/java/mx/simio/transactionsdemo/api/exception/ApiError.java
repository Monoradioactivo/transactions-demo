package mx.simio.transactionsdemo.api.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@JsonPropertyOrder({"message", "httpErrorCode", "httpStatus", "errors", "timestamp"})
public class ApiError {
    private final String message;
    @JsonProperty("http_status")
    private final HttpStatus httpStatus;
    @JsonProperty("http_code")
    private final Integer httpErrorCode;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    public ApiError(String message, HttpStatus httpStatus, Integer httpErrorCode, LocalDateTime timestamp, List<String> errors) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.httpErrorCode = httpErrorCode;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public ApiError(String message, HttpStatus httpStatus, Integer httpErrorCode, LocalDateTime timestamp, String error) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.httpErrorCode = httpErrorCode;
        this.timestamp = timestamp;
        errors = Arrays.asList(error);
    }
}
