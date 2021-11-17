package ru.bachinin.cardealership.dto;

import javax.validation.constraints.NotNull;

public class RequestOrderDTO {
    @NotNull
    private Long id_user;

    @NotNull
    private Long id_vehicle;

    @NotNull
    private Boolean agreement;

    private Integer period;

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public Long getId_vehicle() {
        return id_vehicle;
    }

    public void setId_vehicle(Long id_vehicle) {
        this.id_vehicle = id_vehicle;
    }

    public Boolean getAgreement() {
        return agreement;
    }

    public void setAgreement(Boolean agreement) {
        this.agreement = agreement;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
