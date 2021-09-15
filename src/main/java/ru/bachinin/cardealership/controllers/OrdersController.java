package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.entities.Order;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.enums.OrderStateEnum;
import ru.bachinin.cardealership.enums.VehicleStateEnum;
import ru.bachinin.cardealership.exceptions.BadParamException;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.exceptions.InvalidStateException;
import ru.bachinin.cardealership.exceptions.RequestBodyNotProvidedException;
import ru.bachinin.cardealership.exceptions.ValueNotFoundException;
import ru.bachinin.cardealership.repositories.OrderRepository;
import ru.bachinin.cardealership.repositories.UserRepository;
import ru.bachinin.cardealership.repositories.VehicleRepository;
import ru.bachinin.cardealership.service.ValidationService;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    private final String className = Order.class.getName();
    private final String userClassName = User.class.getName();
    private final String vehicleClassName = Vehicle.class.getName();

    @Autowired
    public OrdersController(OrderRepository orderRepository,
                            UserRepository userRepository,
                            VehicleRepository vehicleRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // Получение списка всех заказов
    @GetMapping()
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Получение конкретного заказа по id
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) throws EntityNotFoundException {
        if (orderRepository.existsById(id)) {
            return orderRepository.findOrderById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @PostMapping("/create")
    public Order createOrder(@RequestBody(required = false) Map<String, Object> requestMap)
            throws RequestBodyNotProvidedException, ValueNotFoundException, BadParamException, EntityNotFoundException,
            InvalidStateException {
        ValidationService.checkMapNullOrEmpty(requestMap);

        String keyUser = "id_user";
        String keyVehicle = "id_vehicle";

        ValidationService.checkMapValue(requestMap, keyUser);
        ValidationService.checkMapValue(requestMap, keyVehicle);

        Order order = new Order();

        Long user_id = ValidationService.parseLong(requestMap, keyUser);
        ValidationService.checkExistence(userRepository, user_id, userClassName);
        User user = userRepository.findUserById(user_id);

        order.setCreatedBy(user);

        Long id_vehicle = ValidationService.parseLong(requestMap, keyVehicle);
        ValidationService.checkExistence(vehicleRepository, id_vehicle, vehicleClassName);
        Vehicle vehicle = vehicleRepository.getVehicleById(id_vehicle);

        if (vehicle.getVehicleStateEnum() != VehicleStateEnum.PROCESSED) {
            throw new InvalidStateException("vehicle");
        }

        order.setVehicle(vehicle);
        order.setCreatedAt(LocalDate.now());
        order.setOrderState(OrderStateEnum.CREATED);

        return orderRepository.save(order);
    }

    @PostMapping()
    public Order createOrder(Order order) {
        order.setCreatedAt(LocalDate.now());
        order.setOrderState(OrderStateEnum.CREATED);
        orderRepository.save(order);
        return order;
    }

    @PutMapping("/cancel")
    public Order cancelOrder(@RequestBody(required = false) Map<String, Long> requestMap)
            throws EntityNotFoundException, InvalidStateException, RequestBodyNotProvidedException, ValueNotFoundException {

        Order order = checkValidOrder(requestMap);

        if (order.getOrderState() != OrderStateEnum.CANCELED) {
            order.setOrderState(OrderStateEnum.CANCELED);
        } else {
            throw new InvalidStateException("order");
        }

        return orderRepository.save(order);
    }

    @PostMapping("/accept")
    public Order acceptOrder(@RequestBody Map<String, Long> requestMap)
            throws RequestBodyNotProvidedException, ValueNotFoundException, EntityNotFoundException, InvalidStateException {

        Order order = checkValidOrder(requestMap);

        if (order.getOrderState() == OrderStateEnum.CREATED) {
            order.setOrderState(OrderStateEnum.ACCEPTED);
        } else {
            throw new InvalidStateException("order");
        }

        return orderRepository.save(order);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id,
                             @RequestBody Order order) throws EntityNotFoundException {
        if (orderRepository.existsById(id)) {
            Optional<Order> optionalOrder = orderRepository.findById(id);
            if (optionalOrder.isPresent()) {
                Order oldOrder = optionalOrder.get();
                oldOrder.setUpdatedAt(LocalDate.now());
                oldOrder.setOrderState(order.getOrderState());
                oldOrder.setVehicle(order.getVehicle());
                oldOrder.setCreatedBy(order.getCreatedBy());
                return orderRepository.save(oldOrder);
            } else {
                throw new EntityNotFoundException(id, className);
            }
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) throws EntityNotFoundException {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    // Проверяет входные параметры на корректность
    // Возвращает экземпляр объекта в случае успешной проверки
    private Order checkValidOrder(Map<String, Long> requestMap)
            throws RequestBodyNotProvidedException, ValueNotFoundException, EntityNotFoundException {

        String keyOrder = "id_order";

        ValidationService.checkMapNullOrEmpty(requestMap);
        ValidationService.checkMapValue(requestMap, keyOrder);

        Long id_order = requestMap.get(keyOrder);
        ValidationService.checkExistence(orderRepository, id_order, className);

        return orderRepository.findOrderById(id_order);
    }
}
