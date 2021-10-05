package ru.bachinin.cardealership.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;


public class RequestInvoiceDTO {
    @NotNull
    private Long id_user;

    @NotEmpty
    private List<LinkedHashMap<String, String>> vehicles;

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public List<LinkedHashMap<String, String>> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<LinkedHashMap<String, String>> vehicles) {
        this.vehicles = vehicles;
    }
}
