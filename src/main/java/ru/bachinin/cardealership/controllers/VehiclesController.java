package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.VehicleRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehiclesController {

    private final VehicleRepository vehicleRepository;
    private final String className = Vehicle.class.getName();

    @Autowired
    public VehiclesController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping()
    public List<Vehicle> getAllVehicle() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle getVehicle(@PathVariable Long id) throws EntityNotFoundException {
        if (vehicleRepository.existsById(id)) {
            return vehicleRepository.getById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @PostMapping()
    public Vehicle createVehicle(Vehicle vehicle) {
        vehicle.setCreatedAt(LocalDate.now());
        return vehicleRepository.save(vehicle);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id,
                                 @RequestBody Vehicle vehicle) throws EntityNotFoundException {
        if (vehicleRepository.existsById(id)) {
            Vehicle oldVehicle = vehicleRepository.getById(id);
            oldVehicle.setUpdatedAt(LocalDate.now());
            oldVehicle.setVIN(vehicle.getVIN());
            oldVehicle.setAvailable(vehicle.getAvailable());
            oldVehicle.setVehicleCost(vehicle.getVehicleCost());
            oldVehicle.setTotalCost(vehicle.getTotalCost());
            oldVehicle.setCreatedAt(vehicle.getCreatedAt());
            oldVehicle.setEngineVolume(vehicle.getEngineVolume());
            return vehicleRepository.save(oldVehicle);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) throws EntityNotFoundException {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }
}
