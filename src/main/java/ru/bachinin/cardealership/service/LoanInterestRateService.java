package ru.bachinin.cardealership.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bachinin.cardealership.entities.LoanInterestRate;
import ru.bachinin.cardealership.repositories.LoanInterestRateRepository;

import javax.annotation.PostConstruct;

@Service
public class LoanInterestRateService {

    private final LoanInterestRateRepository loanInterestRateRepository;

    @Autowired
    public LoanInterestRateService(LoanInterestRateRepository loanInterestRateRepository) {
        this.loanInterestRateRepository = loanInterestRateRepository;
    }

    @PostConstruct
    public void insertLoanInterests() {
        insertLoanInterest(12, 17.9F);
        insertLoanInterest(24, 21.9F);
        insertLoanInterest(36, 22.9F);
        insertLoanInterest(48, 25.9F);
        insertLoanInterest(60, 28.9F);
    }

    private void insertLoanInterest(Integer period, Float rate) {
        if (!loanInterestRateRepository.existsByPeriod(period)) {
            loanInterestRateRepository.save(new LoanInterestRate(rate, period));
        }
    }
}
