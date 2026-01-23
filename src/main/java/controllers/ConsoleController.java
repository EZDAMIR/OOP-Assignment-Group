package controllers;

import models.PcStation;
import models.User;
import repositories.PcStationRepository;
import repositories.UserRepository;
import services.BookingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleController {

    private final UserRepository userRepo;
    private final PcStationRepository pcRepo;
    private final BookingService bookingService;

    public ConsoleController(UserRepository userRepo, PcStationRepository pcRepo, BookingService bookingService) {
        this.userRepo = userRepo;
        this.pcRepo = pcRepo;
        this.bookingService = bookingService;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== PC CLUB BOOKING ===");
            System.out.println("1) Create user");
            System.out.println("2) Add PC station");
            System.out.println("3) List active PCs");
            System.out.println("4) Create booking (business logic)");
            System.out.println("5) List bookings");
            System.out.println("6) Check PC availability");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> createUser(scanner);
                    case "2" -> addPc(scanner);
                    case "3" -> listPcs();
                    case "4" -> createBooking(scanner);
                    case "5" -> listBookings();
                    case "6" -> checkAvailability(scanner);
                    case "0" -> {
                        System.out.println("Bye.");
                        return;
                    }
                    default -> System.out.println("Unknown option.");
                }
            } catch (Exception exception) {
                System.out.println("ERROR: " + exception.getMessage());
            }
        }
    }

    private void createUser(Scanner scanner) {
        System.out.print("Full name: ");
        String name = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        User user = new User(0, name, phone);
        user = userRepo.create(user);
        System.out.println("Created: " + user);
    }

    private void addPc(Scanner scanner) {
        System.out.print("PC code (e.g. PC-01): ");
        String code = scanner.nextLine();
        System.out.print("GPU tier (MID/HIGH): ");
        String tier = scanner.nextLine();
        System.out.print("Base rate per hour (e.g. 1500.00): ");
        BigDecimal rate = new BigDecimal(scanner.nextLine());

        PcStation pc = new PcStation(0, code, tier, rate, true);
        pc = pcRepo.create(pc);
        System.out.println("Added: " + pc);
    }

    private void listPcs() {
        List<PcStation> pcs = pcRepo.findAllActive();
        pcs.forEach(System.out::println);
    }

    private void createBooking(Scanner sc) {
        System.out.print("User ID: ");
        int userId = Integer.parseInt(sc.nextLine());

        System.out.print("PC ID: ");
        int pcId = Integer.parseInt(sc.nextLine());

        System.out.println("Enter start time (format: 2026-01-23T18:00)");
        LocalDateTime start = LocalDateTime.parse(sc.nextLine());

        System.out.println("Enter end time (format: 2026-01-23T21:00)");
        LocalDateTime end = LocalDateTime.parse(sc.nextLine());

        var booking = bookingService.createBooking(userId, pcId, start, end);
        System.out.println("Booking created: " + booking);
    }

    private void listBookings() {
        bookingService.listBookings().forEach(System.out::println);
    }

    private void checkAvailability(Scanner sc) {
        System.out.print("PC ID: ");
        int pcId = Integer.parseInt(sc.nextLine());

        System.out.println("Start time (2026-01-23T18:00): ");
        LocalDateTime start = LocalDateTime.parse(sc.nextLine());

        System.out.println("End time (2026-01-23T21:00): ");
        LocalDateTime end = LocalDateTime.parse(sc.nextLine());

        boolean available = bookingService.isPcAvailable(pcId, start, end);
        System.out.println(available ? "AVAILABLE" : "NOT AVAILABLE");
    }
}
