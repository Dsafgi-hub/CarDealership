package ru.bachinin.cardealership.exceptions;

public class NonUniqueValueException extends RuntimeException {
    public NonUniqueValueException(String value , String className) {
        super("Entity = " + className + " already have value = " + value);
    }
}
