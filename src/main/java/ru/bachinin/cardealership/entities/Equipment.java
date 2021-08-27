package ru.bachinin.cardealership.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "equipments")
public class Equipment implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "equipmentSequence", sequenceName = "EQUIPMENTS_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipmentSequence")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "equipment_type_id", referencedColumnName = "id")
    private TypeOfEquipment typeOfEquipment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
