package co.com.almacena.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.Product;

public interface ProductRepository extends JpaRepository <Product, Long> {
	List<Product> findByTenantIdOrderByNameAsc(Long tenantId);
}
