package ru.bachinin.cardealership.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

}
