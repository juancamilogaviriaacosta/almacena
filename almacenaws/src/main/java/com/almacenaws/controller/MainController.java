package com.almacenaws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.almacenaws.model.InventoryMovement;
import com.almacenaws.model.Product;
import com.almacenaws.service.ProductService;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private ProductService productService;
   
    @GetMapping(path = "getProducts")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @PostMapping(path = "createProduct")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
    	productService.createProduct(product);
        return ResponseEntity.ok("Usuario creado");
    }
    
    @GetMapping(path = "getIventoryMovement")
    public List<InventoryMovement> getIventoryMovement() {
        return productService.getIventoryMovement();
    }
}