package ru.bachinin.cardealership.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterUserDTO {
    @NotBlank
    @Size(min = 3, max = 50)
    private String login;

    @NotBlank
    @Size(min = 3, max = 50)
    private String password;

    @NotBlank
    private String surname;

    @NotBlank
    private String firstName;

    private String secondName;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
