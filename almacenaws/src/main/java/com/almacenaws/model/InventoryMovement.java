package com.almacenaws.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class InventoryMovement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;
	
	@ManyToOne(optional = false)
	private Product product;
	
	@ManyToOne
	private Warehouse warehouse;
	
	@Column
	private Integer quantity;
	
	@Column
	@Enumerated(EnumType.STRING)
	private MovementType movementType;
	
	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime fechahora;
	
	@Column
	private String notes;
	
	@ManyToOne
	private Usuario usuario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public MovementType getMovementType() {
		return movementType;
	}

	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
	}

	public OffsetDateTime getFechahora() {
		return fechahora;
	}

	public void setFechahora(OffsetDateTime fechahora) {
		this.fechahora = fechahora;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}