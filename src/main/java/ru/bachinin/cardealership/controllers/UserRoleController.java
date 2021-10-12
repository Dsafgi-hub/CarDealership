package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.dto.UpdateUserRoleDTO;
import ru.bachinin.cardealership.entities.UserRole;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.UserRoleRepository;
import javax.annotation.PostConstruct;
import java.util.List;

@RestController
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

    @GetMapping()
    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    @GetMapping("/{id}")
    public UserRole getRole(@PathVariable Long id)
            throws EntityNotFoundException {
        if (userRoleRepository.existsById(id)) {
            return userRoleRepository.getById(id);
        } else {
            throw new EntityNotFoundException(id, UserRole.class.getSimpleName());
        }
    }

    @PostMapping()
    public UserRole createRole(@RequestBody UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @PutMapping()
    public UserRole updateRole(@RequestBody UpdateUserRoleDTO updateUserRoleDTO)
            throws EntityNotFoundException {
        if (userRoleRepository.existsById(updateUserRoleDTO.getId())) {
            UserRole oldRole = userRoleRepository.getById(updateUserRoleDTO.getId());
            oldRole.setName(updateUserRoleDTO.getUserRole().getName());
            return userRoleRepository.save(oldRole);
        } else {
            throw new EntityNotFoundException(updateUserRoleDTO.getId(), UserRole.class.getSimpleName());
        }
    }

    @DeleteMapping()
    public void deleteRole(@RequestBody Long id)
            throws EntityNotFoundException {
        if (userRoleRepository.existsById(id)) {
            userRoleRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, UserRole.class.getSimpleName());
        }
    }
}
