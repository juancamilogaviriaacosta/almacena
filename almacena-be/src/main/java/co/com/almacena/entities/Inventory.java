package co.com.almacena.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventories")
public class Inventory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@ManyToOne
    private Tenant tenant;
	
	@ManyToOne
	private Warehouse warehouse;
	
	@ManyToOne
	private Product product;
	
	@Column
	private Integer quantity;
	
	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant fechahora;

	public Inventory() {
	}

	public Inventory(Long id, Tenant tenant, Warehouse warehouse, Product product, Integer quantity,
			Instant fechahora) {
		this.id = id;
		this.tenant = tenant;
		this.warehouse = warehouse;
		this.product = product;
		this.quantity = quantity;
		this.fechahora = fechahora;
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

	public Instant getFechahora() {
		return fechahora;
	}

	public void setFechahora(Instant fechahora) {
		this.fechahora = fechahora;
	}
}