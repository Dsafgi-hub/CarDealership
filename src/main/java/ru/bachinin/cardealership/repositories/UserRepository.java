package ru.bachinin.cardealership.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByLogin(String login);

    User findUserByLoginAndPassword(String login, String password);

    User findUserById(Long id);
}
