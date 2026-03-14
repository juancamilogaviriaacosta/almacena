package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.User;

public interface UserRepository extends JpaRepository <User, Long> {

}
