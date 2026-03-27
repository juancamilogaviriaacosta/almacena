package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.datawarehouse.DimProduct;

public interface DimProductRepository extends JpaRepository <DimProduct, Long> {

}