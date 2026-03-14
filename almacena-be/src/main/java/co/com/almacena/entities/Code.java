package co.com.almacena.entities;

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
@Table(name = "codes")
public class Code {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@ManyToOne
    private Tenant tenant;
	
	@Column(unique = true)
	private String code;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ManyToOne
	private Product product;
	
	@ManyToOne
	private Combo combo;
	
	public Code() {
	}

	public Code(Long id, Tenant tenant, String code, Status status, Product product, Combo combo) {
		this.id = id;
		this.tenant = tenant;
		this.code = code;
		this.status = status;
		this.product = product;
		this.combo = combo;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Combo getCombo() {
		return combo;
	}

	public void setCombo(Combo combo) {
		this.combo = combo;
	}
}