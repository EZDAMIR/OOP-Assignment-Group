package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    private int id;
    private int userId;
    private int pcId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalPrice;
    private String status;

    @Override
    public String toString() {
        return "Booking#" + id +
                " user=" + userId +
                " pc=" + pcId +
                " [" + startTime + " -> " + endTime + "]" +
                " price=" + totalPrice +
                " status=" + status;
    }
}
