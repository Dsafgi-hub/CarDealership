package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.UserRepository;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserRepository userRepository;
    private final String className = User.class.getName();

    @Autowired
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws EntityNotFoundException {
        if (userRepository.existsById(id)) {
            return userRepository.getById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @PostMapping()
    // @ResponseStatus(code = HttpStatus.CREATED)
    public User addUser(User user) {
        user.setCreatedAt(LocalDate.now());
        String password = user.getPassword();
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(password);
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updatePassword(@PathVariable Long id,
                               @RequestBody User user) throws EntityNotFoundException {
        if (userRepository.existsById(id)) {
            User oldUser = userRepository.getById(id);
            oldUser.setFirstName(user.getFirstName());
            oldUser.setLogin(user.getLogin());
            oldUser.setPassword(user.getPassword());
            oldUser.setSecondName(user.getSecondName());
            oldUser.setSurname(user.getSurname());
            oldUser.setUserRole(user.getUserRole());
            return userRepository.save(oldUser);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) throws EntityNotFoundException {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }
}
