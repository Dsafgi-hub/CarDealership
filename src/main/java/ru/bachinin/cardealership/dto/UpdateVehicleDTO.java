package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.entities.Vehicle;

import javax.validation.constraints.NotNull;

public class UpdateVehicleDTO {
    @NotNull
    private Long id;

    @NotNull
    private Vehicle vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
