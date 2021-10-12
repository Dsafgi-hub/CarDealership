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
import ru.bachinin.cardealership.dto.UpdateTypeOfEquipmentDTO;
import ru.bachinin.cardealership.entities.TypeOfEquipment;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.TypeOfEquipmentRepository;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/types_of_equipment")
public class TypeOfEquipmentsController {

    private final TypeOfEquipmentRepository typeOfEquipmentRepository;

    @Autowired
    public TypeOfEquipmentsController(TypeOfEquipmentRepository typeOfEquipmentRepository) {
        this.typeOfEquipmentRepository = typeOfEquipmentRepository;
    }

    @PostConstruct
    public void insertTypes() {
        if (!typeOfEquipmentRepository.existsByName("сигнализация")) {
            typeOfEquipmentRepository.save(new TypeOfEquipment("сигнализация"));
        }

        if (!typeOfEquipmentRepository.existsByName("тонировка всех стёкол")) {
            typeOfEquipmentRepository.save(new TypeOfEquipment("тонировка всех стёкол"));
        }

        if (!typeOfEquipmentRepository.existsByName("антикоррозийная обработка")) {
            typeOfEquipmentRepository.save(new TypeOfEquipment("антикоррозийная обработка"));
        }

        if (!typeOfEquipmentRepository.existsByName("коврики в салон")) {
            typeOfEquipmentRepository.save(new TypeOfEquipment("коврики в салон"));
        }
    }

    @GetMapping()
    public List<TypeOfEquipment> getAllTypes() {
        return typeOfEquipmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public TypeOfEquipment getType(@PathVariable Long id)
            throws EntityNotFoundException {
        if (typeOfEquipmentRepository.existsById(id)) {
            return typeOfEquipmentRepository.getById(id);
        } else {
            throw new EntityNotFoundException(id, TypeOfEquipment.class.getSimpleName());
        }
    }

    @PostMapping()
    public TypeOfEquipment createType(@RequestBody @Valid TypeOfEquipment typeOfEquipment) {
        return typeOfEquipmentRepository.save(typeOfEquipment);
    }

    @PutMapping()
    public TypeOfEquipment updateType(@RequestBody @Valid UpdateTypeOfEquipmentDTO updateTypeOfEquipmentDTO)
            throws EntityNotFoundException {
        if (typeOfEquipmentRepository.existsById(updateTypeOfEquipmentDTO.getId())) {
            TypeOfEquipment oldType = typeOfEquipmentRepository.getById(updateTypeOfEquipmentDTO.getId());
            oldType.setName(updateTypeOfEquipmentDTO.getTypeOfEquipment().getName());
            return typeOfEquipmentRepository.save(oldType);
        } else {
            throw new EntityNotFoundException(updateTypeOfEquipmentDTO.getId(), TypeOfEquipment.class.getSimpleName());
        }
    }

    @DeleteMapping()
    public void deleteType(@RequestBody Long id)
            throws EntityNotFoundException {
        if (typeOfEquipmentRepository.existsById(id)) {
            typeOfEquipmentRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, TypeOfEquipment.class.getSimpleName());
        }
    }
}
