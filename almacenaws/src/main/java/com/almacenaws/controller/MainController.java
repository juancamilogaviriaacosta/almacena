package com.almacenaws.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @PostMapping(path = "updateProduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
    	String response = productService.updateProduct(product);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping(path = "manualMovement/{warehouseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> manualMovement(@PathVariable("warehouseId") Integer warehouseId,
    		@RequestBody List<Map<String, Object>> manualMovement) {
    	productService.manualMovement(warehouseId, manualMovement);
    	return ResponseEntity.ok("Ok");
    }
    
    @GetMapping(path = "getProduct")
    public Map<String, Object> getProduct(@RequestParam("id") Integer id) {
        return productService.getProduct(id);
    }
    
    @GetMapping(path = "getProducts")
    public List<Map<String, Object>> getProducts() {
        return productService.getProducts();
    }
    
    @GetMapping(path = "getInventory")
    public List<Map<String, Object>> getInventory() {
        return productService.getInventory();
    }
    
    @GetMapping(path = "getWarehouse")
    public List<Map<String, Object>> getWarehouse() {
        return productService.getWarehouse();
    }
    
    @GetMapping(path = "getRegister")
    public List<Map<String, Object>> getRegister() {
        return productService.getRegister();
    }
    
    @GetMapping(path = "getInventoryMovement")
    public List<InventoryMovement> getInventoryMovement() {
        return productService.getInventoryMovement();
    }
    
    @PostMapping(path = "uploadFile")
    public Map<String, Integer> uploadFile(@RequestParam("fileId") String fileId,
    		@RequestParam("warehouseId") Integer warehouseId, @RequestParam("file") MultipartFile mpf) {
    	return productService.uploadFile(fileId, warehouseId, mpf);
    }
}