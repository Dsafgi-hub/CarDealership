package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.dto.pojos.RequestInvoiceVehicle;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RequestInvoiceDTO {
    @NotNull
    private Long id_user;

    @NotEmpty
    private List<RequestInvoiceVehicle> vehicles;

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public List<RequestInvoiceVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<RequestInvoiceVehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
