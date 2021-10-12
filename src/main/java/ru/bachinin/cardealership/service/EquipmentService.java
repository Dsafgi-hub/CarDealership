package ru.bachinin.cardealership.service;

import ru.bachinin.cardealership.entities.Vehicle;

public interface EquipmentService {
    void generateEquipment(Vehicle vehicle) throws InterruptedException;
}
