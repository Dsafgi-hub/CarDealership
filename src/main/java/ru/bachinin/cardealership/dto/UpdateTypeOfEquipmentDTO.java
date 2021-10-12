package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.entities.TypeOfEquipment;

import javax.validation.constraints.NotNull;

public class UpdateTypeOfEquipmentDTO {
    @NotNull
    private Long id;

    @NotNull
    private TypeOfEquipment typeOfEquipment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOfEquipment getTypeOfEquipment() {
        return typeOfEquipment;
    }

    public void setTypeOfEquipment(TypeOfEquipment typeOfEquipment) {
        this.typeOfEquipment = typeOfEquipment;
    }
}
