package co.com.almacena.entities;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
	
	@Column
	private String sku;
	
	@OneToMany(mappedBy = "product")
	@JsonManagedReference("product-codes")
	private List<Code> code;
	
	@Column
	private String name;
	
	@Column
	private String category;
	
	@Column
	private BigDecimal price;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Status status;

	public Product() {
	}

	public Product(Long id, Tenant tenant, String sku, List<Code> code, String name, String category, BigDecimal price,
			Status status) {
		this.id = id;
		this.tenant = tenant;
		this.sku = sku;
		this.code = code;
		this.name = name;
		this.category = category;
		this.price = price;
		this.status = status;
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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public List<Code> getCode() {
		return code;
	}

	public void setCode(List<Code> code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}