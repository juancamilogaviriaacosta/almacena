package co.com.almacena.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.Log;

public interface LogRepository extends JpaRepository <Log, Long> {
	List<Log> findByTenantIdOrderByIdDesc(Long tenantId);
}