package ru.bachinin.cardealership.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.bachinin.cardealership.dto.UpdateInvoiceDTO;
import ru.bachinin.cardealership.enums.InvoiceStateEnum;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "invoices", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "defaultSeq", sequenceName = "INVOICES_SEQ", allocationSize = 1)
public class Invoice extends UpdatedAndCreatedBaseEntity implements Serializable {
    @Column(name = "state", nullable = false)
    private InvoiceStateEnum invoiceState;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User createdBy;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "invoice",
            cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;

    public InvoiceStateEnum getInvoiceState() {
        return invoiceState;
    }

    public void setInvoiceState(InvoiceStateEnum invoiceState) {
        this.invoiceState = invoiceState;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void updateInvoice(UpdateInvoiceDTO updateInvoiceDTO) {
        this.setCreatedAt(updateInvoiceDTO.getUpdateInvoice().getCreated_at());
        this.setInvoiceState(InvoiceStateEnum.valueOf(updateInvoiceDTO.getUpdateInvoice().getState()));
    }

}
