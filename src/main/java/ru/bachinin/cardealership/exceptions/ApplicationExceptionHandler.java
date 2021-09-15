package ru.bachinin.cardealership.exceptions;

import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.bachinin.cardealership.dto.Response;
import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NonUniqueValueException.class)
    public ResponseEntity<Response> handleNonUniqueValueException(NonUniqueValueException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValueNotFoundException.class)
    public ResponseEntity<Response> handleValueNotFoundFoundException(ValueNotFoundException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestBodyNotProvidedException.class)
    public ResponseEntity<Response> handleRequestBodyNotProvidedException(RequestBodyNotProvidedException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadParamException.class)
    public ResponseEntity<Response> handleBadParamException(BadParamException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<Response> handleInvalidStateException(InvalidStateException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Response> handleForbiddenException(ForbiddenException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({TypeMismatchException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Response> handleTypeMismatchException(TypeMismatchException e) {
        Response response = new Response("Type mismatch exception:" + e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.GATEWAY_TIMEOUT);
    }

    private Response makeResponse(String message) {
        String responseMessage = String.format("%s %s", LocalDateTime.now(), message);
        return new Response(responseMessage);
    }
}
