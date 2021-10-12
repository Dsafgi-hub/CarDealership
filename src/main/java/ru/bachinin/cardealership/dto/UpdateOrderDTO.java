package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.dto.pojos.UpdateOrder;
import javax.validation.constraints.NotNull;

public class UpdateOrderDTO {
    @NotNull
    private Long id;

    @NotNull
    private UpdateOrder updateOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UpdateOrder getUpdateOrder() {
        return updateOrder;
    }

    public void setUpdateOrder(UpdateOrder updateOrder) {
        this.updateOrder = updateOrder;
    }
}
