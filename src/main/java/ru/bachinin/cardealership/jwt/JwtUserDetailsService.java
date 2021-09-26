package ru.bachinin.cardealership.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.service.implementation.UserServiceImpl;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserServiceImpl userService;

    @Autowired
    public JwtUserDetailsService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User with login: "+ login + " not found");
        }

        return JwtUserFactory.createJwtUser(user);
    }
}
