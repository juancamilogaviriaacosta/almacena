package co.com.almacena.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.com.almacena.entities.Combo;
import co.com.almacena.entities.InventoryMovement;
import co.com.almacena.entities.Product;
import co.com.almacena.entities.Warehouse;
import co.com.almacena.services.InventoryService;

@RestController
public class InventoryController {
	
	@Autowired
    private InventoryService is;
   
    @GetMapping(path = "api/initDatabase")
    public ResponseEntity<String> initDatabase() {
        is.initDatabase();
        return ResponseEntity.ok("Ok");
    }
    
    @PostMapping(path = "api/updateProduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
    	String response = is.updateProduct(product);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping(path = "api/updateCombo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCombo(@RequestBody Combo combo) {
    	String response = is.updateCombo(combo);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping(path = "api/manualMovement/{warehouseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> manualMovement(@PathVariable("warehouseId") Integer warehouseId,
    		@RequestBody List<Map<String, Object>> manualMovement) {
    	is.manualMovement(warehouseId, manualMovement);
    	return ResponseEntity.ok("Ok");
    }
    
    @GetMapping(path = "api/getProduct")
    public Map<String, Object> getProduct(@RequestParam("id") Integer id) {
        return is.getProduct(id);
    }
    
    @GetMapping(path = "api/getProducts")
    public List<Map<String, Object>> getProducts() {
        return is.getProducts();
    }
    
    @GetMapping(path = "api/getCombos")
    public List<Map<String, Object>> getCombos() {
        return is.getCombos();
    }
    
    @GetMapping(path = "api/getCombo")
    public Map<String, Object> getCombo(@RequestParam("id") Integer id) {
        return is.getCombo(id);
    }
    
    @GetMapping(path = "api/getInventory")
    public List<Map<String, Object>> getInventory(
    		@RequestParam("filterDate") String filterDate,
            @RequestParam("warehouseId") Integer warehouseId) {
        return is.getInventory(filterDate, warehouseId);
    }
    
    @GetMapping(path = "api/getWarehouse")
    public List<Warehouse> getWarehouse() {
        return is.getWarehouse();
    }
    
    @GetMapping(path = "api/getRegister")
    public List<Map<String, Object>> getRegister() {
        return is.getRegister();
    }
    
    @GetMapping(path = "api/getInventoryMovement")
    public List<InventoryMovement> getInventoryMovement() {
        return is.getInventoryMovement();
    }
    
    @PostMapping(path = "api/uploadFile")
    public Map<String, Integer> uploadFile(@RequestParam("fileId") String fileId,
    		@RequestParam("warehouseId") Integer warehouseId, @RequestParam("file") MultipartFile mpf) {
    	return is.uploadFile(fileId, warehouseId, mpf);
    }

}
