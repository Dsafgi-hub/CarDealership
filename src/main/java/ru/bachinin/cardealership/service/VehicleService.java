package ru.bachinin.cardealership.service;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bachinin.cardealership.aop.LogExecutionTime;
import ru.bachinin.cardealership.entities.Equipment;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.enums.VehicleStateEnum;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.VehicleRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
@EnableRabbit
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

    public Vehicle findById(Long id)
            throws EntityNotFoundException {
        if (vehicleRepository.existsById(id)) {
            return vehicleRepository.getVehicleById(id);
        } else {
            throw new EntityNotFoundException(id, Vehicle.class.getSimpleName());
        }
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Scheduled(fixedDelayString = "${scheduled.fixed-delay}")
    @LogExecutionTime
    public void manufacturedVehicle()
            throws InterruptedException {
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

    private String generateRandomString()
            throws InterruptedException {
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        int delay = (int) ( Math.random() * 3 ) * 1_000;
        Thread.sleep(delay);

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

    private Double generateEngineVolume()
            throws InterruptedException {
        int a = (int) ( Math.random() * 3 );
        Thread.sleep( (long) a * 1000);
        switch (a) {
            case 0: return 1.6;
            case 1: return 1.8;
            case 2: return 2.0;
            default: return 1.5;
        }
    }

    private void generateEquipment(Vehicle vehicle)
            throws InterruptedException {
        int countEquipments = (int) (1 + Math.random() * 6 );
        for(int i = 0; i < countEquipments; i++) {
            equipmentService.generateEquipment(vehicle);
        }
    }
}
