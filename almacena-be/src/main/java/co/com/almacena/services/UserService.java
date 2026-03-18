package co.com.almacena.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.com.almacena.entities.User;
import co.com.almacena.repositories.UserRepository;
import co.com.almacena.security.JwtService;


@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
    private JwtService jwtService;

	public List<User> findAll() {
		return repo.findAll();
	}

	public ResponseEntity<Map<String, String>> createUser(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		repo.save(user);
		return ResponseEntity.ok(Map.of("message","ok"));
	}

	public User findById(Long id) {
		return repo.findById(id).get();
	}

	public ResponseEntity<Map<String, String>> deleteById(Long id) {
		repo.deleteById(id);
		return ResponseEntity.ok(Map.of("message","deleted"));
	}

	public Map<String,Object> auth(Map<String, String> map) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<User> byUsername = repo.findByUsername(map.get("username"));
		User u = byUsername.isPresent() ? byUsername.get() : null;
		if(u != null && encoder.matches(map.get("password"), u.getPassword())) {
			String token = jwtService.generateToken(u.getUsername(), u.getId(), u.getRole().name(), u.getTenant().getId(), u.getTenant().getName());
			Map<String,Object> response = new HashMap<>();
            response.put("token", token);
			return response;
		}
		return null;
	}

}
