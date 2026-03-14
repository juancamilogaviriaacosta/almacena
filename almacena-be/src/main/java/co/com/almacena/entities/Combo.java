package co.com.almacena.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "combos")
public class Combo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@ManyToOne
    private Tenant tenant;
	
	@Column
	private String name;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@OneToMany
	private List<ProductDetail> productDetail;
	
	@OneToMany(mappedBy = "combo")
	private List<Code> code;

	public Combo() {
	}

	public Combo(Long id, Tenant tenant, String name, Status status, List<ProductDetail> productDetail,
			List<Code> code) {
		this.id = id;
		this.tenant = tenant;
		this.name = name;
		this.status = status;
		this.productDetail = productDetail;
		this.code = code;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<ProductDetail> getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(List<ProductDetail> productDetail) {
		this.productDetail = productDetail;
	}

	public List<Code> getCode() {
		return code;
	}

	public void setCode(List<Code> code) {
		this.code = code;
	}
}