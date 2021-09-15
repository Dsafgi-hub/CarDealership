package ru.bachinin.cardealership.exceptions;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(Long id, String className) {
        super("Entity = " + className + " is not found by id = " + id);
    }

    public EntityNotFoundException(String name, String className) {
        super("Entity = " + className + " is not found by name = " + name);
    }
}
