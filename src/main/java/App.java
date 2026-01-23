import controllers.ConsoleController;
import config.ConnectionManager;
import repositories.BookingRepository;
import repositories.PcStationRepository;
import repositories.UserRepository;
import repositories.jdbc.JdbcBookingRepository;
import repositories.jdbc.JdbcPcStationRepository;
import repositories.jdbc.JdbcUserRepository;
import services.BookingService;
import services.PeakHoursPricingPolicy;
import services.PricingPolicy;

public class App {
    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager();

        UserRepository userRepo = new JdbcUserRepository(connectionManager);
        PcStationRepository pcRepo = new JdbcPcStationRepository(connectionManager);
        BookingRepository bookingRepo = new JdbcBookingRepository(connectionManager);

        PricingPolicy pricingPolicy = new PeakHoursPricingPolicy();

        BookingService bookingService = new BookingService(userRepo, pcRepo, bookingRepo, pricingPolicy);

        ConsoleController controller = new ConsoleController(userRepo, pcRepo, bookingService);
        controller.run();
    }
}
