package services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class PeakHoursPricingPolicy implements PricingPolicy {

    @Override
    public BigDecimal calculate(BigDecimal baseRatePerHour, LocalDateTime start, LocalDateTime end) {
        long minutes = Duration.between(start, end).toMinutes();
        if (minutes <= 0) throw new IllegalArgumentException("End time must be after start time");

        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        boolean peak = start.getHour() >= 18 && start.getHour() < 23;

        BigDecimal multiplier = peak ? new BigDecimal("1.25") : BigDecimal.ONE;

        BigDecimal raw = baseRatePerHour.multiply(hours).multiply(multiplier);

        boolean discount = hours.compareTo(new BigDecimal("3.00")) >= 0;
        if (discount) raw = raw.multiply(new BigDecimal("0.90"));

        return raw.setScale(2, RoundingMode.HALF_UP);
    }
}
