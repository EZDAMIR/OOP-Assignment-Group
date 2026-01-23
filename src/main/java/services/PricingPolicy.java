package services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PricingPolicy {
    BigDecimal calculate(BigDecimal baseRatePerHour, LocalDateTime start, LocalDateTime end);
}
