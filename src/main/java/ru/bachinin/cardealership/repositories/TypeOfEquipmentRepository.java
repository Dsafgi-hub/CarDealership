package ru.bachinin.cardealership.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.TypeOfEquipment;

@Repository
public interface TypeOfEquipmentRepository extends JpaRepository<TypeOfEquipment, Long> {
}
