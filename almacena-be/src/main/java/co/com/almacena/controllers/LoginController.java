package co.com.almacena.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.com.almacena.entities.User;
import co.com.almacena.services.UserService;



@RestController
public class LoginController {
	
	@Autowired
	private UserService us;
	
	@PostMapping(path = "api/auth")
    public User auth(@RequestBody Map<String, String> map) {
		return us.auth(map);
	}

}
