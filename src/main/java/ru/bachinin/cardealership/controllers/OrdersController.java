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
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.OrderRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderRepository orderRepository;
    private final String className = Order.class.getName();

    @Autowired
    public OrdersController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping()
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) throws EntityNotFoundException {
        if (orderRepository.existsById(id)) {
            return orderRepository.getById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @PostMapping()
    public Order createOrder(Order order) {
        order.setCreatedAt(LocalDate.now());
        return orderRepository.save(order);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id,
                             @RequestBody Order order) throws EntityNotFoundException {
        if (orderRepository.existsById(id)) {
            Order oldOrder = orderRepository.getById(id);
            oldOrder.setUpdatedAt(LocalDate.now());
            oldOrder.setOrderState(order.getOrderState());
            oldOrder.setVehicle(order.getVehicle());
            oldOrder.setCreatedBy(order.getCreatedBy());
            return orderRepository.save(oldOrder);
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
}
