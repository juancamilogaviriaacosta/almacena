package co.com.almacena.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.almacena.entities.User;
import co.com.almacena.repositories.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;

	public List<User> findAll() {
		return repo.findAll();
	}

	public ResponseEntity<Map<String, String>> createUser(User user) {
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

	public User auth(Map<String, String> map) {
		Optional<User> byUsername = repo.findByUsername(map.get("username"));
		if(byUsername.isPresent() && byUsername.get().getPassword().equals(map.get("password"))) {
			return byUsername.get();
		}
		return null;
	}

}
