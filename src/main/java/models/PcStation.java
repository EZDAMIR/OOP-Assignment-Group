package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PcStation {
    private int id;
    private String code;
    private String gpuTier;
    private BigDecimal baseRatePerHour;
    private boolean active;

    @Override
    public String toString() {
        return id + ": " + code + " tier=" + gpuTier + " rate=" + baseRatePerHour + "/h active=" + active;
    }
}
