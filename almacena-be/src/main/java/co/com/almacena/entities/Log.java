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
@Table(name = "logs")
public class Log {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@ManyToOne
    private Tenant tenant;
	
	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant fechahora;
	
	@Column(columnDefinition = "TEXT")
	private String information;

	public Log() {
	}

	public Log(Long id, Tenant tenant, Instant fechahora, String information) {
		this.id = id;
		this.tenant = tenant;
		this.fechahora = fechahora;
		this.information = information;
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

	public Instant getFechahora() {
		return fechahora;
	}

	public void setFechahora(Instant fechahora) {
		this.fechahora = fechahora;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}
}