package co.com.almacena.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.User;

public interface UserRepository extends JpaRepository <User, Long> {
	Optional<User> findByUsername(String username);
}
