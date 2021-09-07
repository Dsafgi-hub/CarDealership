package ru.bachinin.cardealership.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
