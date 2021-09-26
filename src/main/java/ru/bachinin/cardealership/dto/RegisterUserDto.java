package ru.bachinin.cardealership.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterUserDto {

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

}
