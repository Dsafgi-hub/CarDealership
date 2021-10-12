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
import ru.bachinin.cardealership.dto.UpdateVehicleModelDTO;
import ru.bachinin.cardealership.entities.VehicleModel;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.VehicleModelRepository;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vehicle_models")
public class VehicleModelsController {

    private final VehicleModelRepository vehicleModelRepository;

    @Autowired
    public VehicleModelsController(VehicleModelRepository vehicleModelRepository) {
        this.vehicleModelRepository = vehicleModelRepository;
    }

    @PostConstruct
    public void insertModels() {
        if (!vehicleModelRepository.existsByName("Luxury")) {
            vehicleModelRepository.save(new VehicleModel("Luxury"));
        }

        if (!vehicleModelRepository.existsByName("Basic")) {
            vehicleModelRepository.save(new VehicleModel("Basic"));
        }

        if (!vehicleModelRepository.existsByName("Standard")) {
            vehicleModelRepository.save(new VehicleModel("Standard"));
        }

        if (!vehicleModelRepository.existsByName("Premium")) {
            vehicleModelRepository.save(new VehicleModel("Premium"));
        }
    }

    @GetMapping()
    public List<VehicleModel> getAllVehicleModel() {
        return vehicleModelRepository.findAll();
    }

    @GetMapping("/{id}")
    public VehicleModel getVehicleModel(@PathVariable Long id)
            throws EntityNotFoundException {
        if (vehicleModelRepository.existsById(id)) {
            return vehicleModelRepository.getById(id);
        } else {
            throw new EntityNotFoundException(id, VehicleModel.class.getName());
        }
    }

    @PostMapping()
    public VehicleModel createVehicleModel(@RequestBody @Valid VehicleModel vehicleModel) {
        return vehicleModelRepository.save(vehicleModel);
    }

    @PutMapping()
    public VehicleModel updateVehicleModel(@RequestBody @Valid UpdateVehicleModelDTO updateVehicleModelDTO)
            throws EntityNotFoundException {
        Long id = updateVehicleModelDTO.getId();

        if (vehicleModelRepository.existsById(id)) {
            VehicleModel vehicleModel = vehicleModelRepository.getById(id);
            vehicleModel.setName(updateVehicleModelDTO.getVehicleModel().getName());
            return vehicleModelRepository.save(vehicleModel);
        } else {
            throw new EntityNotFoundException(id, VehicleModel.class.getName());
        }
    }

    @DeleteMapping()
    public void deleteVehicleModel(@RequestBody Long id)
            throws EntityNotFoundException {
        if (vehicleModelRepository.existsById(id)) {
            vehicleModelRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, VehicleModel.class.getName());
        }
    }
}
