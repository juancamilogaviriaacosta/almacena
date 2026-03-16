package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.Warehouse;

public interface WarehouseRepository extends JpaRepository <Warehouse, Long> {

}
