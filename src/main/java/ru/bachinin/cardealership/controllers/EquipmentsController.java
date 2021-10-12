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
import ru.bachinin.cardealership.dto.UpdateEquipmentDTO;
import ru.bachinin.cardealership.entities.Equipment;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.EquipmentRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentsController {

    public final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentsController(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping()
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Equipment getEquipment(@PathVariable Long id) throws EntityNotFoundException {
        if (equipmentRepository.existsById(id)) {
            return equipmentRepository.getById(id);
        } else {
            throw new EntityNotFoundException(id, Equipment.class.getSimpleName());
        }
    }

    @PostMapping()
    public Equipment createEquipment(@RequestBody @Valid Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @PutMapping("/{id}")
    public Equipment updateEquipment(@RequestBody @Valid UpdateEquipmentDTO updateEquipmentDTO)
            throws EntityNotFoundException {
        Long id = updateEquipmentDTO.getId();
        if (equipmentRepository.existsById(id)) {
            Equipment oldEquipment = equipmentRepository.getById(id);
            oldEquipment.updateEquipment(updateEquipmentDTO.getEquipment());
            return equipmentRepository.save(oldEquipment);
        } else {
            throw new EntityNotFoundException(id, Equipment.class.getSimpleName());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Long id) throws EntityNotFoundException {
        if (equipmentRepository.existsById(id)) {
            equipmentRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, Equipment.class.getSimpleName());
        }
    }
}
