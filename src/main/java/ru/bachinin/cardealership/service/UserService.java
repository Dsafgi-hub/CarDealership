package ru.bachinin.cardealership.service;

import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;

public interface UserService {
    User register(User user);

    Iterable<User> findAll();

    User findByLogin(String login);

    User save(User user);

    User findById(Long id) throws EntityNotFoundException;

    void existsById(Long id) throws EntityNotFoundException;

    void deleteById(Long id) throws EntityNotFoundException;

}
