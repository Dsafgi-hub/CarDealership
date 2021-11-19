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

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ResponseDTO> handleInvalidStateException(InvalidStateException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LowCreditRatingException.class)
    public ResponseEntity<ResponseDTO> handleLowCreditRatingException(LowCreditRatingException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ResponseDTO makeResponse(String message) {
        String responseMessage = String.format("%s %s", LocalDateTime.now(), message);
        return new ResponseDTO(responseMessage);
    }
}
