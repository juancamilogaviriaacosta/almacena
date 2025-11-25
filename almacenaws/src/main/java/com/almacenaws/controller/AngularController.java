package com.almacenaws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AngularController {
	
	//@RequestMapping(value = "/{path:[^\\.]*}/**")
	@GetMapping(value = {"/{path:[^\\.]+}", "/{path:[^\\.]+}/**"})
    public String redirect() {
        return "forward:/index.html";
    }
}


