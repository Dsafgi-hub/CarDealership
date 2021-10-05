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
@Table(name = "types_of_equipment", schema = "public")
@SequenceGenerator(name = "defaultSeq", sequenceName = "TYPES_OF_EQUIPMENT_SEQ", allocationSize = 1)
public class TypeOfEquipment extends BaseEntity implements Serializable {
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "typeOfEquipment")
    private List<Equipment> equipmentList;

    public TypeOfEquipment() {
    }

    public TypeOfEquipment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }
}
