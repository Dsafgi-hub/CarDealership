package ru.bachinin.cardealership.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
