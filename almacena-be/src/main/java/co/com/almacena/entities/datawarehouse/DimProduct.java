package co.com.almacena.entities.datawarehouse;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "datalake", name = "dim_product")
public class DimProduct {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String asin;
    
	@Column
	private String title;
	
	@Column
	private String category;
	
	@Column
	private String brand;
    
	@Column
	private String imageUrl;
	
	@Column
	private String productUrl;    

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

	public DimProduct() {
	}

	public DimProduct(Long id, String asin, String title, String category, String brand, String imageUrl, String productUrl, Instant createdAt) {
		this.id = id;
		this.asin = asin;
		this.title = title;
		this.category = category;
		this.brand = brand;
		this.imageUrl = imageUrl;
		this.productUrl = productUrl;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
}