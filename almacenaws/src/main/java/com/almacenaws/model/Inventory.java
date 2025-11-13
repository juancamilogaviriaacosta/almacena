package com.almacenaws.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Inventory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;
	
	@ManyToOne
	private Warehouse warehouse;
	
	@ManyToOne
	private Product product;
	
	@Column
	private Integer quantity;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private OffsetDateTime fechahora;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public OffsetDateTime getFechahora() {
		return fechahora;
	}

	public void setFechahora(OffsetDateTime fechahora) {
		this.fechahora = fechahora;
	}
}