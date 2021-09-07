package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.entities.VehicleModel;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.VehicleModelRepository;

import java.util.List;

@RestController
@RequestMapping("/vehicle_models")
public class VehicleModelsController {

    private final VehicleModelRepository vehicleModelRepository;
    private final String className = VehicleModel.class.getName();

    @Autowired
    public VehicleModelsController(VehicleModelRepository vehicleModelRepository) {
        this.vehicleModelRepository = vehicleModelRepository;
    }

    @GetMapping()
    public List<VehicleModel> getAllVehicleModel() {
        return vehicleModelRepository.findAll();
    }

    @GetMapping("/{id}")
    public VehicleModel getVehicleModel(@PathVariable Long id) throws EntityNotFoundException {
        if (vehicleModelRepository.existsById(id)) {
            return vehicleModelRepository.getById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @PostMapping()
    public VehicleModel createVehicleModel(VehicleModel vehicleModel) {
        return vehicleModelRepository.save(vehicleModel);
    }

    @PutMapping("/{id}")
    public VehicleModel updateVehicleModel(@PathVariable Long id,
                                           @RequestParam(name = "name") String name) throws EntityNotFoundException {
        if (vehicleModelRepository.existsById(id)) {
            VehicleModel vehicleModel = vehicleModelRepository.getById(id);
            vehicleModel.setName(name);
            return vehicleModelRepository.save(vehicleModel);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteVehicleModel(@PathVariable Long id) throws EntityNotFoundException {
        if (vehicleModelRepository.existsById(id)) {
            vehicleModelRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }
}
