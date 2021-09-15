package ru.bachinin.cardealership.exceptions;

public class BadParamException extends Exception {
    public BadParamException(String key, String targetClass) {
        super("Bad parameter for key " + key + ", " + targetClass + " expected");
    }
}
