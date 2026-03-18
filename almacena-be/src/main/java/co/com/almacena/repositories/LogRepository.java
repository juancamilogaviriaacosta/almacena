package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.Log;

public interface LogRepository extends JpaRepository <Log, Long> {

}