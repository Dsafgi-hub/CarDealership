package ru.bachinin.cardealership.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "loan_interest_rates", schema = "public")
@SequenceGenerator(name = "defaultSeq", sequenceName = "LOAN_INTEREST_RATES_SEQ", allocationSize = 1)
public class LoanInterestRate extends BaseEntity implements Serializable {

    @Column(name = "rate", nullable = false)
    private Float rate;

    @Column(name = "period", nullable = false)
    private Integer period;

    public LoanInterestRate(Float rate, Integer period) {
        this.rate = rate;
        this.period = period;
    }

    public LoanInterestRate() {
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
