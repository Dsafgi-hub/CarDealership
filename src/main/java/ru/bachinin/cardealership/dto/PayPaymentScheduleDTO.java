package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.entities.User;

import javax.validation.constraints.NotNull;

public class PayPaymentScheduleDTO {

    @NotNull
    private Integer id_user;

    @NotNull
    private Float payment_value;
}
