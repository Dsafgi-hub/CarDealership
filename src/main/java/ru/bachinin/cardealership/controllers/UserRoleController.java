package ru.bachinin.cardealership.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bachinin.cardealership.entities.UserRole;
import ru.bachinin.cardealership.repositories.UserRoleRepository;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping("/user_roles")
public class UserRoleController {

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleController(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @PostConstruct
    public void insertRoles() {
        if (!userRoleRepository.existsByName("ROLE_USER")) {
            userRoleRepository.save(new UserRole("ROLE_USER"));
        }

        if (!userRoleRepository.existsByName("ROLE_MANAGER")) {
            userRoleRepository.save(new UserRole("ROLE_MANAGER"));
        }

        if (!userRoleRepository.existsByName("ROLE_ADMINISTRATOR")) {
            userRoleRepository.save(new UserRole("ROLE_ADMINISTRATOR"));
        }
    }
}
