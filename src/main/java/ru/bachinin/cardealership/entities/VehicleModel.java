package ru.bachinin.cardealership.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "vehicle_model")
public class VehicleModel implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "vehicleModelSequence", sequenceName = "VEHICLE_MODEL_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicleModelSequence")
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
