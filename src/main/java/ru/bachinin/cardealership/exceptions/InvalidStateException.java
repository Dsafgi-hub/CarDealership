package ru.bachinin.cardealership.exceptions;

public class InvalidStateException extends RuntimeException{
    public InvalidStateException(String entityName) {
        super("Entity " + entityName + " already has invalid state for changing");
    }
}
