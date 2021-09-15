package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.entities.Invoice;
import ru.bachinin.cardealership.entities.Order;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.enums.UserRoleEnum;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.exceptions.NonUniqueValueException;
import ru.bachinin.cardealership.exceptions.ValueNotFoundException;
import ru.bachinin.cardealership.repositories.InvoiceRepository;
import ru.bachinin.cardealership.repositories.OrderRepository;
import ru.bachinin.cardealership.repositories.UserRepository;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;

    private final String className = User.class.getName();
    private final String orderClassName = Order.class.getName();
    private final String invoiceClassName = Invoice.class.getName();

    @Autowired
    public UsersController(UserRepository userRepository,
                           OrderRepository orderRepository,
                           InvoiceRepository invoiceRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
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
                                              @RequestParam(defaultValue = "id") String sortBy) throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Invoice> invoicePage = invoiceRepository.findAllByCreatedBy_Id(id, pageable);
        if (invoicePage.hasContent()) {
            return invoicePage;
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }



    @PostMapping(value = "/register")
    // @ResponseStatus(code = HttpStatus.CREATED)
    public User addUser(@RequestBody User user)
            throws NonUniqueValueException, ValueNotFoundException {
        if (user.getLogin() == null) {
            throw new ValueNotFoundException("login");
        }

        if (user.getPassword() == null) {
            throw new ValueNotFoundException("password");
        }

        if (user.getSurname() == null) {
            throw new ValueNotFoundException("surname");
        }

        if (user.getFirstName() == null) {
            throw new ValueNotFoundException("firstName");
        }

        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new NonUniqueValueException(user.getLogin(), className);
        }

        user.setCreatedAt(LocalDate.now());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setUserRole(UserRoleEnum.USER);

        return userRepository.save(user);
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
