package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.entities.UserRole;

import javax.validation.constraints.NotNull;

public class UpdateUserRoleDTO {
    @NotNull
    private Long id;

    @NotNull
    private UserRole userRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
