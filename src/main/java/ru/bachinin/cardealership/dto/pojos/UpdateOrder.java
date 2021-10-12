package ru.bachinin.cardealership.dto.pojos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateOrder {
    @NotBlank
    private String orderState;

    @NotNull
    private Long vehicle;

    @NotNull
    private Long createdBy;

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Long getVehicle() {
        return vehicle;
    }

    public void setVehicle(Long vehicle) {
        this.vehicle = vehicle;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
