package repositories;

import models.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository {
    Booking create(Booking booking);

    boolean existsOverlapActive(int pcId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAll();
}
