package co.com.almacena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.almacena.entities.datawarehouse.DimRank;

public interface DimRankRepository extends JpaRepository <DimRank, Long> {

}