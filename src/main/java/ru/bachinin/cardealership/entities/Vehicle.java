package ru.bachinin.cardealership.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.bachinin.cardealership.enums.VehicleStateEnum;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vehicles", schema = "public")
public class Vehicle implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "vehicleSequence", sequenceName = "VEHICLES_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicleSequence")
    private Long id;

    @Column(name = "VIN", unique = true)
    private String VIN;

    @Column(name = "colour", nullable = false)
    private String colour;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "vehicle_cost")
    private BigDecimal vehicleCost;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "engine_volume")
    private Double engineVolume;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_State", nullable = false)
    private VehicleStateEnum vehicleStateEnum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehicle_model", referencedColumnName = "id", nullable = false)
    private VehicleModel vehicleModel;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "vehicle")
    private List<Equipment> equipments;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "vehicle")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_invoice", referencedColumnName = "id")
    private Invoice invoice;

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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
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

    public VehicleStateEnum getVehicleStateEnum() {
        return vehicleStateEnum;
    }

    public void setVehicleStateEnum(VehicleStateEnum vehicleStateEnum) {
        this.vehicleStateEnum = vehicleStateEnum;
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

}
