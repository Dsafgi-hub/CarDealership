package ru.bachinin.cardealership.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.LoanInterestRate;

@Repository
public interface LoanInterestRateRepository extends JpaRepository<LoanInterestRate, Long> {
    LoanInterestRate findLoanInterestRateRepositoriesByPeriod(Integer period);

    boolean existsByPeriod(Integer period);
}
