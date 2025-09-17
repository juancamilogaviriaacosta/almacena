package com.almacenaws.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.almacenaws.model.InventoryMovement;
import com.almacenaws.model.Product;
import com.almacenaws.service.ProductService;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private ProductService productService;
   
    @GetMapping(path = "initDatabase")
    public ResponseEntity<String> initDatabase() {
        productService.initDatabase();
        return ResponseEntity.ok("Ok");
    }
    
    @GetMapping(path = "getProducts")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @PostMapping(path = "createProduct")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
    	productService.createProduct(product);
        return ResponseEntity.ok("Producto creado");
    }
    
    @GetMapping(path = "getInventory")
    public List<Map<String, Object>> getInventory() {
        return productService.getInventory();
    }
    
    @GetMapping(path = "getInventoryMovement")
    public List<InventoryMovement> getInventoryMovement() {
        return productService.getInventoryMovement();
    }
    
    @PostMapping(path = "uploadFile")
    public Map<String, Integer> uploadFile(@RequestParam("id") String id, @RequestParam("file") MultipartFile mpf) {
    	return productService.uploadFile(id, mpf);
    }
}