package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.Inventory;

public interface InventoryRepository extends JpaRepository <Inventory, Long> {

}