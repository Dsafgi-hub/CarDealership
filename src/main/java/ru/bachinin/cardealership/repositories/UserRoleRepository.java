package ru.bachinin.cardealership.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bachinin.cardealership.entities.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(String name);
}
