package ru.bachinin.cardealership.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bachinin.cardealership.entities.Equipment;
import ru.bachinin.cardealership.entities.TypeOfEquipment;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.repositories.EquipmentRepository;
import ru.bachinin.cardealership.repositories.TypeOfEquipmentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Transactional
public class EquipmentService {

    private static final BigDecimal MIN_EQUIPMENT_COST = new BigDecimal("1e3");
    private static final BigDecimal MAX_EQUIPMENT_COST = new BigDecimal("7.5e4");

    private final EquipmentRepository equipmentRepository;
    private final TypeOfEquipmentRepository typeOfEquipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository,
                            TypeOfEquipmentRepository typeOfEquipmentRepository) {
        this.equipmentRepository = equipmentRepository;
        this.typeOfEquipmentRepository = typeOfEquipmentRepository;
    }

    public void generateEquipment(Vehicle vehicle) {
        Equipment equipment = new Equipment();
        equipment.setVehicle(vehicle);
        equipment.setPrice(generateEquipmentCost());

        generateEquipmentType(equipment);

        generateEquipmentName(equipment);

        vehicle.getEquipments().add(equipment);
        equipmentRepository.save(equipment);
    }

    private BigDecimal generateEquipmentCost() {
        BigDecimal randomBigDecimal = MIN_EQUIPMENT_COST.add(
                BigDecimal.valueOf(Math.random()).multiply(MAX_EQUIPMENT_COST.subtract(MIN_EQUIPMENT_COST))
        );
        return randomBigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    private void generateEquipmentType(Equipment equipment) {
        Long countEquipmentType = typeOfEquipmentRepository.countTypeOfEquipments();
        Long randomId = (long) (Math.random()*countEquipmentType + 1);
        TypeOfEquipment typeOfEquipment = typeOfEquipmentRepository.getById(randomId);
        equipment.setTypeOfEquipment(typeOfEquipment);
    }

    private void generateEquipmentName(Equipment equipment) {
        String[] firstWord = {"Новый ", "Лучший ", "Не имеющий аналогов ", "Сделано в Корее. ", "Сделано в России. "};
        String secondWorld = typeOfEquipmentRepository.getById(equipment.getTypeOfEquipment().getId()).getName();

        int positionRandomFirstWord = (int) (Math.random() * firstWord.length);

        equipment.setName(firstWord[positionRandomFirstWord] + secondWorld);
    }
}
