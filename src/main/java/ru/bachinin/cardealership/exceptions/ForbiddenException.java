package ru.bachinin.cardealership.exceptions;

public class ForbiddenException extends Exception {
    public ForbiddenException() {
        super("Not enough rights");
    }
}
