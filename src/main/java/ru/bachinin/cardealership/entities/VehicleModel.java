package ru.bachinin.cardealership.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "vehicle_models", schema = "public")
@SequenceGenerator(name = "defaultSeq", sequenceName = "VEHICLE_MODELS_SEQ", allocationSize = 1)
public class VehicleModel extends BaseEntity implements Serializable {
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "vehicleModel")
    private List<Vehicle> vehicleList;

    public VehicleModel() {
    }

    public VehicleModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
