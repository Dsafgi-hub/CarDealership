package ru.bachinin.cardealership.entities;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.bachinin.cardealership.dto.pojos.UpdateOrder;
import ru.bachinin.cardealership.enums.OrderStateEnum;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "orders", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "defaultSeq", sequenceName = "ORDERS_SEQ", allocationSize = 1)
public class Order  extends UpdatedAndCreatedBaseEntity implements Serializable {
    @Column(name = "state", nullable = false)
    private OrderStateEnum orderState;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_vehicle", referencedColumnName = "id")
    private Vehicle vehicle;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User createdBy;

    public OrderStateEnum getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderStateEnum orderState) {
        this.orderState = orderState;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

}
