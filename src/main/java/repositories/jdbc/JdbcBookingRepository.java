package repositories.jdbc;

import config.ConnectionManager;
import models.Booking;
import repositories.BookingRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcBookingRepository implements BookingRepository {

    private final ConnectionManager connectionManager;

    public JdbcBookingRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Booking create(Booking booking) {
        String sql = """
                INSERT INTO bookings(user_id, pc_id, start_time, end_time, total_price, status)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, booking.getUserId());
            preparedStatement.setInt(2, booking.getPcId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            preparedStatement.setBigDecimal(5, booking.getTotalPrice());
            preparedStatement.setString(6, booking.getStatus());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) booking.setId(resultSet.getInt("id"));
            }
            return booking;

        } catch (SQLException e) {
            throw new RuntimeException("Booking create failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsOverlapActive(int pcId, LocalDateTime start, LocalDateTime end) {
        String sql = """
                SELECT 1
                FROM bookings
                WHERE pc_id = ?
                  AND status = 'ACTIVE'
                  AND start_time < ?
                  AND end_time > ?
                LIMIT 1;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, pcId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(end));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(start));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Overlap check failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Booking> findAll() {
        String sql = """
                SELECT id, user_id, pc_id, start_time, end_time, total_price, status
                FROM bookings
                ORDER BY id;
                """;

        List<Booking> result = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                result.add(new Booking(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("pc_id"),
                        resultSet.getTimestamp("start_time").toLocalDateTime(),
                        resultSet.getTimestamp("end_time").toLocalDateTime(),
                        resultSet.getBigDecimal("total_price"),
                        resultSet.getString("status")
                ));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Booking findAll failed: " + e.getMessage(), e);
        }
    }
}
