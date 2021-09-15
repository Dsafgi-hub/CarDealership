package ru.bachinin.cardealership.exceptions;

public class ValueNotFoundException extends Exception{
    public ValueNotFoundException(String key) {
        super("Can't find value by key = " + key);
    }
}
