package ru.bachinin.cardealership.dto;

import ru.bachinin.cardealership.dto.pojos.UpdateInvoice;
import javax.validation.constraints.NotNull;

public class UpdateInvoiceDTO {
    @NotNull
    private Long id_invoice;

    @NotNull
    public UpdateInvoice updateInvoice;

    public Long getId_invoice() {
        return id_invoice;
    }

    public void setId_invoice(Long id_invoice) {
        this.id_invoice = id_invoice;
    }


    public UpdateInvoice getUpdateInvoice() {
        return updateInvoice;
    }

    public void setUpdateInvoice(UpdateInvoice updateInvoice) {
        this.updateInvoice = updateInvoice;
    }
}