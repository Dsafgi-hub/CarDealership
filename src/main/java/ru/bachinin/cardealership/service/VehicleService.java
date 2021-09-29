package ru.bachinin.cardealership.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bachinin.cardealership.aop.LogExecutionTime;
import ru.bachinin.cardealership.entities.Equipment;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.enums.VehicleStateEnum;
import ru.bachinin.cardealership.repositories.VehicleRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleService {

    private static final int VIN_LENGTH = 16;
    private static final BigDecimal MIN_VEHICLE_COST = new BigDecimal("5e3");
    private static final BigDecimal MAX_VEHICLE_COST = new BigDecimal("5e6");

    private final VehicleRepository vehicleRepository;
    private final EquipmentService equipmentService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository,
                          EquipmentService equipmentService) {
        this.vehicleRepository = vehicleRepository;
        this.equipmentService = equipmentService;
    }

    @Scheduled(fixedDelay = 300000)
    @LogExecutionTime
    public void manufacturedVehicle() {
        Iterable<Vehicle> vehicleList = vehicleRepository.findAllByVehicleStateEnumEquals(VehicleStateEnum.CREATED);
        for (Vehicle vehicle : vehicleList) {

            vehicle.setVIN(generateRandomString());
            vehicle.setVehicleCost(generateVehicleCost());

            vehicle.setEngineVolume(generateEngineVolume());
            vehicle.setUpdatedAt(LocalDate.now());

            vehicle.setEquipments(new ArrayList<>());
            generateEquipment(vehicle);

            vehicle.setTotalCost(vehicle.getVehicleCost().add(getEquipmentCost(vehicle)));

            vehicle.setVehicleStateEnum(VehicleStateEnum.PROCESSED);
            vehicleRepository.save(vehicle);
        }
    }

    private String generateRandomString() {
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return new Random().ints(VIN_LENGTH, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    private BigDecimal generateVehicleCost() {
        BigDecimal randomBigDecimal = MIN_VEHICLE_COST.add(BigDecimal.valueOf(Math.random()).multiply(MAX_VEHICLE_COST.subtract(MIN_VEHICLE_COST)));
        return randomBigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getEquipmentCost(Vehicle vehicle) {
        BigDecimal equipmentsSum = BigDecimal.ZERO;
        for (Equipment equipment: vehicle.getEquipments()) {
            equipmentsSum = equipmentsSum.add(equipment.getPrice());
        }
        return equipmentsSum;
    }

    private Double generateEngineVolume() {
        int a = (int) ( Math.random() * 3 );
        switch (a) {
            case 0: return 1.6;
            case 1: return 1.8;
            case 2: return 2.0;
            default: return 1.5;
        }
    }

    private void generateEquipment(Vehicle vehicle) {
        int countEquipments = (int) (1 + Math.random() * 6 );
        for(int i = 0; i < countEquipments; i++) {
            equipmentService.generateEquipment(vehicle);
        }
    }
}
