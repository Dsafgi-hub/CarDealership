package ru.bachinin.cardealership.exceptions;

public class LowCreditRatingException extends RuntimeException {
    public LowCreditRatingException(String string) {
        super(string);
    }
}
