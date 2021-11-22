package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.dto.RequestOrderDTO;
import ru.bachinin.cardealership.dto.UpdateOrderDTO;
import ru.bachinin.cardealership.entities.Order;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.enums.OrderStateEnum;
import ru.bachinin.cardealership.enums.VehicleStateEnum;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.exceptions.InvalidStateException;
import ru.bachinin.cardealership.exceptions.PeriodNotStatedException;
import ru.bachinin.cardealership.repositories.OrderRepository;
import ru.bachinin.cardealership.service.BankService;
import ru.bachinin.cardealership.service.UserService;
import ru.bachinin.cardealership.service.VehicleService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final BankService bankService;

    @Autowired
    public OrdersController(OrderRepository orderRepository,
                            UserService userService,
                            VehicleService vehicleService,
                            BankService bankService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.bankService = bankService;
    }

    // Получение списка всех заказов
    @GetMapping()
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Получение конкретного заказа по id
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id)
            throws EntityNotFoundException {
        return findById(id);
    }

    // Получение всех заказов конкретного пользователя
    @GetMapping("/users/{id_user}")
    public Page<Order> getAllOrdersByUserId(@PathVariable Long id_user,
                                            @RequestParam(defaultValue = "0") Integer pageNo,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(defaultValue = "id") String sortBy)
            throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> orderPage = orderRepository.findAllByCreatedBy_Id(id_user, pageable);
        if (orderPage.hasContent()) {
            return orderPage;
        } else {
            throw new EntityNotFoundException(id_user, User.class.getSimpleName());
        }
    }

    // Получение конкретного заказа пользователя
    @GetMapping("{id_order}/users/{id_user}")
    public Order getOrderByIdAndUserId(@PathVariable Long id_user,
                                       @PathVariable Long id_order)
            throws EntityNotFoundException {

        userService.existsById(id_user);

        if (orderRepository.existsById(id_order)) {
            return orderRepository.findOrderByIdAndCreatedBy_Id(id_order, id_user);
        } else {
            throw new EntityNotFoundException(id_order, Order.class.getSimpleName());
        }
    }

    @PostMapping("/create")
    public Order createOrder(@RequestBody RequestOrderDTO requestOrderDTO)
            throws EntityNotFoundException, InvalidStateException {

        Order order = new Order();

        User user = userService.findById(requestOrderDTO.getId_user());
        order.setCreatedBy(user);

        Vehicle vehicle = vehicleService.findById(requestOrderDTO.getId_vehicle());

        if (vehicle.getVehicleStateEnum() != VehicleStateEnum.PROCESSED) {
            throw new InvalidStateException("vehicle");
        }
        order.setVehicle(vehicle);

        BigDecimal vehicleCost = vehicle.getVehicleCost();
        Integer period = 1;
        Float loanRate = 1F;
        if (requestOrderDTO.getAgreement()) {
            if (requestOrderDTO.getPeriod() == null) {
                throw new PeriodNotStatedException("Period is null");
            }

            period = requestOrderDTO.getPeriod();
            loanRate = bankService.findLoanInterestRate(period);
        }
        vehicle.setVehicleCost(vehicleCost);
        bankService.calculateVehicleCost(vehicle, loanRate, period, user);

        vehicle.setVehicleStateEnum(VehicleStateEnum.DONE);
        vehicleService.save(vehicle);

        order.setOrderState(OrderStateEnum.CREATED);
        return orderRepository.save(order);
    }

    @PostMapping("/cancel")
    public Order cancelOrder(@RequestBody Long id)
            throws InvalidStateException, EntityNotFoundException {

        Order order = findById(id);

        if (order.getOrderState() != OrderStateEnum.CANCELED) {
            order.setOrderState(OrderStateEnum.CANCELED);
        } else {
            throw new InvalidStateException("order");
        }
        Vehicle vehicle = order.getVehicle();
        vehicle.setVehicleStateEnum(VehicleStateEnum.PROCESSED);
        vehicleService.save(vehicle);

        return orderRepository.save(order);
    }

    @PostMapping("/accept")
    public Order acceptOrder(@RequestBody Long id)
            throws EntityNotFoundException, InvalidStateException {

        Order order = findById(id);

        if (order.getOrderState() == OrderStateEnum.CREATED) {
            order.setOrderState(OrderStateEnum.ACCEPTED);
        } else {
            throw new InvalidStateException("order");
        }

        return orderRepository.save(order);
    }

    @PutMapping()
    public Order updateOrder(@RequestBody UpdateOrderDTO updateOrderDTO)
            throws EntityNotFoundException {
        if (orderRepository.existsById(updateOrderDTO.getId())) {
            Order oldOrder = findById(updateOrderDTO.getId());
            oldOrder.setOrderState(OrderStateEnum.valueOf(updateOrderDTO.getUpdateOrder().getOrderState()));
            oldOrder.setVehicle(vehicleService.findById(updateOrderDTO.getUpdateOrder().getVehicle()));
            oldOrder.setCreatedBy(userService.findById(updateOrderDTO.getUpdateOrder().getCreatedBy()));
            return orderRepository.save(oldOrder);
        } else {
            throw new EntityNotFoundException(updateOrderDTO.getId(), Order.class.getSimpleName());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id)
            throws EntityNotFoundException {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, Order.class.getSimpleName());
        }
    }

    private Order findById(Long id)
            throws EntityNotFoundException {
        if (orderRepository.existsById(id)) {
            return orderRepository.findOrderById(id);
        } else {
            throw new EntityNotFoundException(id, Order.class.getSimpleName());
        }
    }
}
