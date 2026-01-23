package repositories.jdbc;

import config.ConnectionManager;
import models.PcStation;
import repositories.PcStationRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPcStationRepository implements PcStationRepository {

    private final ConnectionManager connectionManager;

    public JdbcPcStationRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public PcStation create(PcStation pc) {
        String sql = """
                INSERT INTO pc_stations(code, gpu_tier, base_rate_per_hour, is_active)
                VALUES (?, ?, ?, ?)
                RETURNING id;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setString(1, pc.getCode());
            prepareStatement.setString(2, pc.getGpuTier());
            prepareStatement.setBigDecimal(3, pc.getBaseRatePerHour());
            prepareStatement.setBoolean(4, pc.isActive());

            try (ResultSet resultSet = prepareStatement.executeQuery()) {
                if (resultSet.next()) pc.setId(resultSet.getInt("id"));
            }
            return pc;

        } catch (SQLException e) {
            throw new RuntimeException("PC create failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<PcStation> findById(int id) {
        String sql = "SELECT id, code, gpu_tier, base_rate_per_hour, is_active FROM pc_stations WHERE id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setInt(1, id);

            try (ResultSet resultSet = prepareStatement.executeQuery()) {
                if (!resultSet.next()) return Optional.empty();
                return Optional.of(new PcStation(
                        resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("gpu_tier"),
                        resultSet.getBigDecimal("base_rate_per_hour"),
                        resultSet.getBoolean("is_active")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("PC findById failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PcStation> findAllActive() {
        String sql = "SELECT id, code, gpu_tier, base_rate_per_hour, is_active FROM pc_stations WHERE is_active = TRUE ORDER BY id;";
        List<PcStation> result = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                result.add(new PcStation(
                        resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("gpu_tier"),
                        resultSet.getBigDecimal("base_rate_per_hour"),
                        resultSet.getBoolean("is_active")
                ));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("PC findAllActive failed: " + e.getMessage(), e);
        }
    }
}
