package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.Combo;

public interface ComboRepository extends JpaRepository<Combo, Long> {
	
}