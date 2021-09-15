package ru.bachinin.cardealership.exceptions;

public class InvalidStateException extends Exception{
    public InvalidStateException(String entityName) {
        super("Entity " + entityName + " already has invalid state for changing");
    }
}
