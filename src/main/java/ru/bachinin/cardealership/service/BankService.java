package ru.bachinin.cardealership.service;

import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.entities.Vehicle;

public interface BankService {
    Float findLoanInterestRate(Integer period);

    void calculateVehicleCost(Vehicle vehicle, Float loanRate, Integer period, User user);

}
