package ru.bachinin.cardealership.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "vehicleSequence", sequenceName = "VEHICLES_GENERATOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicleSequence")
    private Long id;

    @Column(name = "VIN")
    private String VIN;

    @Column(name = "colour", nullable = false)
    private String colour;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "vehicle_cost")
    private BigDecimal vehicleCost;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "engine_volume")
    private Double engineVolume;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private VehicleModel vehicleModel;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Equipment> equipments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getVehicleCost() {
        return vehicleCost;
    }

    public void setVehicleCost(BigDecimal vehicleCost) {
        this.vehicleCost = vehicleCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Double getEngineVolume() {
        return engineVolume;
    }

    public void setEngineVolume(Double engineVolume) {
        this.engineVolume = engineVolume;
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public Set<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Set<Equipment> equipments) {
        this.equipments = equipments;
    }
}
