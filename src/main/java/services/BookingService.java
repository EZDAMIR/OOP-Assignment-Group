package services;

import models.Booking;
import models.PcStation;
import repositories.BookingRepository;
import repositories.PcStationRepository;
import repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingService {

    private final UserRepository userRepo;
    private final PcStationRepository pcRepo;
    private final BookingRepository bookingRepo;
    private final PricingPolicy pricingPolicy;

    public BookingService(UserRepository userRepo,
                          PcStationRepository pcRepo,
                          BookingRepository bookingRepo,
                          PricingPolicy pricingPolicy) {
        this.userRepo = userRepo;
        this.pcRepo = pcRepo;
        this.bookingRepo = bookingRepo;
        this.pricingPolicy = pricingPolicy;
    }

    public Booking createBooking(int userId, int pcId, LocalDateTime start, LocalDateTime end) {
        userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        PcStation pcStation = pcRepo.findById(pcId).orElseThrow(() -> new IllegalArgumentException("PC not found: " + pcId));

        if (!pcStation.isActive()) throw new IllegalStateException("PC is not active");

        if (bookingRepo.existsOverlapActive(pcId, start, end)) {
            throw new IllegalStateException("This PC is already booked for that time slot.");
        }

        BigDecimal total = pricingPolicy.calculate(pcStation.getBaseRatePerHour(), start, end);

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setPcId(pcId);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setTotalPrice(total);
        booking.setStatus("ACTIVE");

        return bookingRepo.create(booking);
    }

    public List<Booking> listBookings() {
        return bookingRepo.findAll();
    }

    public boolean isPcAvailable(int pcId, LocalDateTime start, LocalDateTime end) {
        return !bookingRepo.existsOverlapActive(pcId, start, end);
    }
}
