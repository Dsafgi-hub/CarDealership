package ru.bachinin.cardealership.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.TypeOfEquipment;

import java.util.List;

@Repository
public interface TypeOfEquipmentRepository extends JpaRepository<TypeOfEquipment, Long> {
    @Query(value = "select count(*) from type_of_equipment", nativeQuery = true)
    Long countTypeOfEquipments();

}
