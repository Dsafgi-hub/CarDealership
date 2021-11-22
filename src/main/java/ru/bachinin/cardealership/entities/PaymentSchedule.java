package ru.bachinin.cardealership.entities;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.bachinin.cardealership.enums.PaymentState;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments_schedule", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "defaultSeq", sequenceName = "PAYMENTS_SCHEDULE_SEQ", allocationSize = 1)
public class PaymentSchedule extends UpdatedAndCreatedBaseEntity {

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "payment_value", nullable = false)
    private BigDecimal paymentValue;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_state", nullable = false)
    private PaymentState paymentState;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "id_user", referencedColumnName = "id", nullable = false)
    private User user;

    public PaymentSchedule(LocalDate paymentDate, BigDecimal paymentValue, PaymentState paymentState, User user) {
        this.paymentDate = paymentDate;
        this.paymentValue = paymentValue;
        this.paymentState = paymentState;
        this.user = user;
    }

    public PaymentSchedule() {
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(BigDecimal paymentValue) {
        this.paymentValue = paymentValue;
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


