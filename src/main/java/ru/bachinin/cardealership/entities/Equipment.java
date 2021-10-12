package ru.bachinin.cardealership.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "equipments", schema = "public")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@SequenceGenerator(name = "defaultSeq", sequenceName = "EQUIPMENTS_SEQ", allocationSize = 1)
public class Equipment extends BaseEntity implements Serializable  {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_equipment_type", referencedColumnName = "id")
    private TypeOfEquipment typeOfEquipment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_vehicle", referencedColumnName = "id")
    private Vehicle vehicle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public TypeOfEquipment getTypeOfEquipment() {
        return typeOfEquipment;
    }

    public void setTypeOfEquipment(TypeOfEquipment typeOfEquipment) {
        this.typeOfEquipment = typeOfEquipment;
    }

    public void updateEquipment(Equipment newEquipment) {
        this.setName(newEquipment.getName());
        this.setPrice(newEquipment.getPrice());
        this.setVehicle(newEquipment.getVehicle());
        this.setTypeOfEquipment(newEquipment.getTypeOfEquipment());
    }
}
