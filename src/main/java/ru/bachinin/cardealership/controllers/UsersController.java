package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.dto.AuthRequestDTO;
import ru.bachinin.cardealership.dto.RegisterUserDTO;
import ru.bachinin.cardealership.dto.UpdateUserDTO;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.exceptions.NonUniqueValueException;
import ru.bachinin.cardealership.jwt.JwtProvider;
import ru.bachinin.cardealership.mappers.RegisterUserDtoMapper;
import ru.bachinin.cardealership.service.UserService;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Autowired
    public UsersController(AuthenticationManager authenticationManager,
                           JwtProvider jwtProvider,
                           UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    // Получение списка всех пользователей
    @GetMapping()
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    // Получение конкретного пользователя
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id)
            throws EntityNotFoundException {
        return userService.findById(id);
    }

    @PostMapping(value = "/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public User addUser(@RequestBody @Valid RegisterUserDTO registerUserDto)
            throws NonUniqueValueException {

        String login = registerUserDto.getLogin();

        if (userService.findByLogin(login) != null) {
            throw new NonUniqueValueException(login, User.class.getSimpleName());
        }

        User user = RegisterUserDtoMapper.REGISTER_USER_DTO_MAPPER.registerUserDtoToUser(registerUserDto);

        return userService.register(user);
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> auth(@RequestBody @Valid AuthRequestDTO requestDto) {
        try {
            String login = requestDto.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, requestDto.getPassword()));
            User user = userService.findByLogin(login);

            if (user == null) {
                throw new UsernameNotFoundException("User with login: " + login + " not found");
            }

            String token = jwtProvider.generateToken(login);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", login);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PutMapping("/{id}")
    public User updatePassword(@RequestBody @Valid UpdateUserDTO updateUserDTO)
            throws EntityNotFoundException {
        Long id = updateUserDTO.getId();
        User oldUser = userService.findById(id);
        oldUser.updateUser(updateUserDTO.getUser());
        return userService.save(oldUser);

    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id)
            throws EntityNotFoundException {
        userService.deleteById(id);
    }
}
