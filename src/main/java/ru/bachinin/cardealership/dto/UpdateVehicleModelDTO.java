package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.entities.VehicleModel;
import javax.validation.constraints.NotNull;

public class UpdateVehicleModelDTO {
    @NotNull
    private Long id;

    @NotNull
    private VehicleModel vehicleModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
}
