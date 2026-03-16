package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.Tenant;

public interface TenantRepository extends JpaRepository <Tenant, Long> {

}
