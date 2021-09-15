package ru.bachinin.cardealership.exceptions;

public class RequestBodyNotProvidedException extends Exception {
    public RequestBodyNotProvidedException() {
        super("Json or request param not found (or they are empty)");
    }
}
