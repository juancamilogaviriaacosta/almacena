package co.com.almacena.entities.datawarehouse;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(schema = "datalake", name = "dim_rank")
public class DimRank {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@ManyToOne
	private DimProduct dimProduct;
	
	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;
	
	@Column
    private Integer currentRank;

	@Column
    private Integer previousRank;

	public DimRank() {
	}

	public DimRank(Long id, DimProduct dimProduct, Instant createdAt, Integer currentRank, Integer previousRank) {
		this.id = id;
		this.dimProduct = dimProduct;
		this.createdAt = createdAt;
		this.currentRank = currentRank;
		this.previousRank = previousRank;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DimProduct getDimProduct() {
		return dimProduct;
	}

	public void setDimProduct(DimProduct dimProduct) {
		this.dimProduct = dimProduct;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getCurrentRank() {
		return currentRank;
	}

	public void setCurrentRank(Integer currentRank) {
		this.currentRank = currentRank;
	}

	public Integer getPreviousRank() {
		return previousRank;
	}

	public void setPreviousRank(Integer previousRank) {
		this.previousRank = previousRank;
	}
}
