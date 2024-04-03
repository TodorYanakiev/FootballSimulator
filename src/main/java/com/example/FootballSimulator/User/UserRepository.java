package com.example.FootballSimulator.User;
import com.example.FootballSimulator.User.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {
    public User getUserByUsername(@Param("username") String username);
    public User getUserByEmail(@Param("email") String email);
}