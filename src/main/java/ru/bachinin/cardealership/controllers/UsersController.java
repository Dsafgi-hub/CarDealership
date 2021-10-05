package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.dto.AuthRequestDTO;
import ru.bachinin.cardealership.dto.RegisterUserDTO;
import ru.bachinin.cardealership.entities.Invoice;
import ru.bachinin.cardealership.entities.Order;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.exceptions.NonUniqueValueException;
import ru.bachinin.cardealership.jwt.JwtProvider;
import ru.bachinin.cardealership.mappers.RegisterUserDtoMapper;
import ru.bachinin.cardealership.repositories.InvoiceRepository;
import ru.bachinin.cardealership.repositories.OrderRepository;
import ru.bachinin.cardealership.repositories.UserRepository;
import ru.bachinin.cardealership.service.implementation.UserServiceImpl;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserServiceImpl userService;

    private final String className = User.class.getName();
    private final String orderClassName = Order.class.getName();
    private final String invoiceClassName = Invoice.class.getName();

    @Autowired
    public UsersController(UserRepository userRepository,
                           OrderRepository orderRepository,
                           InvoiceRepository invoiceRepository,
                           AuthenticationManager authenticationManager,
                           JwtProvider jwtProvider,
                           UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    // Получение списка всех пользователей
    @GetMapping()
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Получение конкретного пользователя
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws EntityNotFoundException {
        if (userRepository.existsById(id)) {
            return userRepository.findUserById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    // Получение всех заказов конкретного пользователя
    @GetMapping("/{id}/orders")
    public Page<Order> getAllOrdersByUserId(@PathVariable Long id,
                                            @RequestParam(defaultValue = "0") Integer pageNo,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(defaultValue = "id") String sortBy) throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> orderPage = orderRepository.findAllByCreatedBy_Id(id, pageable);
        if (orderPage.hasContent()) {
            return orderPage;
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    // Получение конкретного заказа пользователя
    @GetMapping("{id}/orders/{id_order}")
    public Order getOrderByIdAndUserId(@PathVariable Long id,
                                       @PathVariable Long id_order) throws EntityNotFoundException {
        if (userRepository.existsById(id)) {
            if (orderRepository.existsById(id_order)) {
                return orderRepository.findOrderByIdAndCreatedBy_Id(id_order, id);
            } else {
                throw new EntityNotFoundException(id_order, orderClassName);
            }
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    // Получение всех накладных конкретного пользователя
    @GetMapping("/{id}/invoices")
    public Page<Invoice> getAllInvoicesByUserId(@PathVariable Long id,
                                                @RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(defaultValue = "id") String sortBy)
            throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Invoice> invoicePage = invoiceRepository.findAllByCreatedBy_Id(id, pageable);
        if (invoicePage.hasContent()) {
            return invoicePage;
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @PostMapping(value = "/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody RegisterUserDTO registerUserDto)
            throws NonUniqueValueException {

        String login = registerUserDto.getLogin();

        if (userRepository.findByLogin(login) != null) {
            throw new NonUniqueValueException(login, className);
        }

        User user = RegisterUserDtoMapper.REGISTER_USER_DTO_MAPPER.registerUserDtoToUser(registerUserDto);

        return userService.register(user);
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> auth(@RequestBody AuthRequestDTO requestDto) {
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
    public User updatePassword(@PathVariable Long id,
                               @RequestBody User user) throws EntityNotFoundException {
        if (userRepository.existsById(id)) {
            User oldUser = userRepository.findUserById(id);
            oldUser.setUpdatedAt(LocalDate.now());
            oldUser.setFirstName(user.getFirstName());
            oldUser.setLogin(user.getLogin());
            oldUser.setPassword(user.getPassword());
            oldUser.setSecondName(user.getSecondName());
            oldUser.setSurname(user.getSurname());
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
