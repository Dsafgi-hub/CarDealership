package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.entities.User;

import javax.validation.constraints.NotNull;

public class UpdateUserDTO {
    @NotNull
    private Long id;

    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
