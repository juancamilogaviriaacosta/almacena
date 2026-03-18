package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.Code;

public interface CodeRepository extends JpaRepository <Code, Long> {

}