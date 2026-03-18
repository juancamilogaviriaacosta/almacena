package co.com.almacena.security;

public class UserDto {
	
	private Long id;
    private Long tenantId;	
    private String username;	
    private String name;	
    private String email;
    private String role;
    private String password;
    
	public UserDto() {
	}
	
	public UserDto(Long id, Long tenantId, String username, String name, String email, String role, String password) {
		this.id = id;
		this.tenantId = tenantId;
		this.username = username;
		this.name = name;
		this.email = email;
		this.role = role;
		this.password = password;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
