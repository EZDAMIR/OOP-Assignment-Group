package repositories.jdbc;

import config.ConnectionManager;
import models.User;
import repositories.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final ConnectionManager connectionManager;

    public JdbcUserRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users(full_name, phone) VALUES (?, ?) RETURNING id;";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getPhone());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) user.setId(resultSet.getInt("id"));
            }
            return user;

        } catch (SQLException e) {
            throw new RuntimeException("User create failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT id, full_name, phone FROM users WHERE id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return Optional.empty();
                return Optional.of(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("phone")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("User findById failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT id, full_name, phone FROM users ORDER BY id;";
        List<User> result = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                result.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("phone")
                ));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException("User findAll failed: " + e.getMessage(), e);
        }
    }
}
