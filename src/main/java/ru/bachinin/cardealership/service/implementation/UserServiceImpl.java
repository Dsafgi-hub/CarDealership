package ru.bachinin.cardealership.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.entities.UserRole;
import ru.bachinin.cardealership.repositories.UserRepository;
import ru.bachinin.cardealership.repositories.UserRoleRepository;
import ru.bachinin.cardealership.service.UserService;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private BCryptPasswordEncoder passwordEncoder;


    public UserServiceImpl() {}

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        UserRole userRole = userRoleRepository.findByName("ROLE_USER");
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        return userRepository.save(user);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
