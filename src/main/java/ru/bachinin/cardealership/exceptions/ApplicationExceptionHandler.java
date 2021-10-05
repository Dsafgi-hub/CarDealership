package ru.bachinin.cardealership.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.bachinin.cardealership.dto.ResponseDTO;
import java.time.LocalDateTime;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NonUniqueValueException.class)
    public ResponseEntity<ResponseDTO> handleNonUniqueValueException(NonUniqueValueException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValueNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleValueNotFoundFoundException(ValueNotFoundException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestBodyNotProvidedException.class)
    public ResponseEntity<ResponseDTO> handleRequestBodyNotProvidedException(RequestBodyNotProvidedException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadParamException.class)
    public ResponseEntity<ResponseDTO> handleBadParamException(BadParamException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ResponseDTO> handleInvalidStateException(InvalidStateException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseDTO> handleForbiddenException(ForbiddenException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

//    @ExceptionHandler({TypeMismatchException.class, MethodArgumentTypeMismatchException.class})
//    public ResponseEntity<Response> handleTypeMismatchException(TypeMismatchException e) {
//        Response response = new Response("Type mismatch exception:" + e.getMessage());
//
//        return new ResponseEntity<>(response, HttpStatus.GATEWAY_TIMEOUT);
//    }

    private ResponseDTO makeResponse(String message) {
        String responseMessage = String.format("%s %s", LocalDateTime.now(), message);
        return new ResponseDTO(responseMessage);
    }
}
