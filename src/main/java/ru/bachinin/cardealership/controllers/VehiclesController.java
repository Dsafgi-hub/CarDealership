package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.dto.UpdateVehicleDTO;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.enums.VehicleStateEnum;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.VehicleRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/vehicles")
public class VehiclesController {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehiclesController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping()
    public Page<Vehicle> getAllVehicle(@RequestParam(defaultValue = "0") Integer pageNo,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return vehicleRepository.findAll(pageable);
    }

    @GetMapping("/created")
    public Page<Vehicle> getAllCreatedVehicle(@RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return vehicleRepository.findAllByVehicleStateEnumEquals(VehicleStateEnum.CREATED, pageable);
    }

    @GetMapping("/processed")
    public Page<Vehicle> getAllProcessedVehicle(@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return vehicleRepository.findAllByVehicleStateEnumEquals(VehicleStateEnum.PROCESSED, pageable);
    }

    @GetMapping("/{id}")
    public Vehicle getVehicle(@PathVariable Long id)
            throws EntityNotFoundException {
        if (vehicleRepository.existsById(id)) {
            return vehicleRepository.getVehicleById(id);
        } else {
            throw new EntityNotFoundException(id, Vehicle.class.getName());
        }
    }

    @GetMapping("/invoice/{id_invoice}")
    public Page<Vehicle> getAllVehiclesByIdInvoice(@PathVariable Long id_invoice,
                                                   @RequestParam(defaultValue = "0") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam(defaultValue = "id") String sortBy)
            throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Vehicle> vehiclePage = vehicleRepository.findAllByInvoiceId(id_invoice, pageable);
        if (vehiclePage.hasContent()) {
            return vehiclePage;
        } else {
            throw new EntityNotFoundException(id_invoice, "Invoice");
        }
    }

    @PostMapping()
    public Vehicle createVehicle(@RequestBody @Valid Vehicle vehicle) {
        vehicle.setVehicleStateEnum(VehicleStateEnum.CREATED);
        return vehicleRepository.save(vehicle);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@RequestBody @Valid UpdateVehicleDTO updateVehicleDTO)
            throws EntityNotFoundException {
        Long id = updateVehicleDTO.getId();
        if (vehicleRepository.existsById(id)) {
            Vehicle oldVehicle = vehicleRepository.getVehicleById(id);
            oldVehicle.updateVehicle(updateVehicleDTO.getVehicle());
            return vehicleRepository.save(oldVehicle);
        } else {
            throw new EntityNotFoundException(id, Vehicle.class.getName());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id)
            throws EntityNotFoundException {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, Vehicle.class.getName());
        }
    }
}
