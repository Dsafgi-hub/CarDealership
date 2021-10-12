package ru.bachinin.cardealership.dto.pojos;

import javax.validation.constraints.NotBlank;

public class RequestInvoiceVehicle {
    @NotBlank
    private String colour;

    @NotBlank
    private String vehicle_model;

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }
}
