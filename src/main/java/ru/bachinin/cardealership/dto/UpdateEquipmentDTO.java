package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.entities.Equipment;

import javax.validation.constraints.NotNull;

public class UpdateEquipmentDTO {
    @NotNull
    private Long id;

    @NotNull
    private Equipment equipment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
}
