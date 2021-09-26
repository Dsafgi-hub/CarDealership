package ru.bachinin.cardealership.service;

import ru.bachinin.cardealership.entities.User;

public interface UserService {
    User register(User user);

    User findByLogin(String login);
}
