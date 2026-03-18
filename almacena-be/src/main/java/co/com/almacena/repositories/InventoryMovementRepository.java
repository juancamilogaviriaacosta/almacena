package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.InventoryMovement;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
	
}