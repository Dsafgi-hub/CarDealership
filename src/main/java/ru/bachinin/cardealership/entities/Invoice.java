package ru.bachinin.cardealership.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.bachinin.cardealership.enums.InvoiceStateEnum;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "invoices", schema = "public")
@SequenceGenerator(name = "defaultSeq", sequenceName = "INVOICES_SEQ", allocationSize = 1)
public class Invoice extends BaseEntity implements Serializable {
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "state", nullable = false)
    private InvoiceStateEnum invoiceState;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User createdBy;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "invoice",
            cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

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

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

}
