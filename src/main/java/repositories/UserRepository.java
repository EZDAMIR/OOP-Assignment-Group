package repositories;

import models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);
    Optional<User> findById(int id);
    List<User> findAll();
}
