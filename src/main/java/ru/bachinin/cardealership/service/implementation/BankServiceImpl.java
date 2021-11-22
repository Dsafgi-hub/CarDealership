package ru.bachinin.cardealership.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bachinin.cardealership.entities.PaymentSchedule;
import ru.bachinin.cardealership.entities.User;
import ru.bachinin.cardealership.entities.Vehicle;
import ru.bachinin.cardealership.enums.PaymentState;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.exceptions.LowCreditRatingException;
import ru.bachinin.cardealership.mappers.UserRatingDtoMapper;
import ru.bachinin.cardealership.proxies.CreditRatingFeignProxy;
import ru.bachinin.cardealership.repositories.LoanInterestRateRepository;
import ru.bachinin.cardealership.repositories.PaymentScheduleRepository;
import ru.bachinin.cardealership.service.BankService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class BankServiceImpl implements BankService {

    private static final int MIN_CREDIT_RATING = -7;

    private final LoanInterestRateRepository loanInterestRateRepository;
    private final CreditRatingFeignProxy creditRatingFeignProxy;
    private final UserRatingDtoMapper userRatingDtoMapper;
    private final PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    public BankServiceImpl(LoanInterestRateRepository loanInterestRateRepository,
                           CreditRatingFeignProxy creditRatingFeignProxy,
                           UserRatingDtoMapper userRatingDtoMapper,
                           PaymentScheduleRepository paymentScheduleRepository) {
        this.loanInterestRateRepository = loanInterestRateRepository;
        this.creditRatingFeignProxy = creditRatingFeignProxy;
        this.userRatingDtoMapper = userRatingDtoMapper;
        this.paymentScheduleRepository = paymentScheduleRepository;
    }

    @Override
    public Float findLoanInterestRate(Integer period) {
        Float rate = loanInterestRateRepository.findLoanInterestRateRepositoriesByPeriod(period).getRate();

        if (rate == null) {
            throw new EntityNotFoundException(period.toString(), LoanInterestRateRepository.class.getSimpleName());
        }

        return rate;
    }

    @Override
    public void calculateVehicleCost(Vehicle vehicle, Float loanRate, Integer period, User user) {
        if (period > 1) {
            Integer creditRating = getUserCreditRating(user);
            loanRate = creditRating <= 0 ? loanRate + (loanRate/2) : loanRate - (loanRate/10);
        }

        BigDecimal onePayment = calculateMonthPayment(vehicle, loanRate, period);


        makePaymentSchedule(user, period, onePayment);
    }

    private Integer getUserCreditRating(User user) {
        Integer creditRating =  creditRatingFeignProxy.calculateRating(userRatingDtoMapper.userToUserRatingDto(user));

        if (creditRating != null
                && creditRating < MIN_CREDIT_RATING) {
            throw new LowCreditRatingException("Your credit rating is low");
        }

        return creditRating;
    }

    private BigDecimal calculateMonthPayment(Vehicle vehicle, Float loanRate, Integer period) {
        return vehicle.getTotalCost().multiply(BigDecimal.valueOf(1 + (loanRate / 100)))
                .divide(new BigDecimal(period), RoundingMode.CEILING);
    }

    private void makePaymentSchedule(User user, Integer period, BigDecimal vehicleCost) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = period > 1 ? startDate.plusYears(period/12) : startDate;

        while (!startDate.isAfter(endDate) && !startDate.isEqual(endDate)) {
            paymentScheduleRepository.save(new PaymentSchedule(startDate, vehicleCost, PaymentState.DUE, user));
            startDate = startDate.plusMonths(1);
        }
    }
}
