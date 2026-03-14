package co.com.almacena.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@ManyToOne
    private Tenant tenant;
	
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
    private Instant fechahora;
	
	@Column
	private String notes;
	
	@ManyToOne
	private User user;

	public InventoryMovement() {
	}

	public InventoryMovement(Long id, Tenant tenant, Product product, Warehouse warehouse, Integer quantity,
			MovementType movementType, Instant fechahora, String notes, User user) {
		this.id = id;
		this.tenant = tenant;
		this.product = product;
		this.warehouse = warehouse;
		this.quantity = quantity;
		this.movementType = movementType;
		this.fechahora = fechahora;
		this.notes = notes;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
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

	public Instant getFechahora() {
		return fechahora;
	}

	public void setFechahora(Instant fechahora) {
		this.fechahora = fechahora;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}