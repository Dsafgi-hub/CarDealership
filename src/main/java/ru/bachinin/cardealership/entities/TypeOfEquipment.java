package ru.bachinin.cardealership.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "type_of_equipment")
public class TypeOfEquipment implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "typeOfEquipmentSequence", sequenceName = "TYPE_OF_EQUIPMENT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "typeOfEquipmentSequence")
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;



}
