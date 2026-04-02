package co.com.almacena.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.com.almacena.entities.Combo;
import co.com.almacena.entities.InventoryMovement;
import co.com.almacena.entities.Log;
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
    
    @GetMapping(path = "api/runEtl")
    public ResponseEntity<String> runEtl() {
        is.runEtl();
        return ResponseEntity.ok("Ok");
    }
    
    @GetMapping(path = "api/getProductRanks")
    public ResponseEntity<List<Object[]>> getProductRanks(@RequestParam("criteria") String criteria,
    		@RequestParam("start") LocalDate start, @RequestParam("end") LocalDate end) {
        List<Object[]> data = is.getProductRanks(criteria, start, end);
		return ResponseEntity.ok(data);
    }
    
    @PostMapping(path = "api/updateProduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProduct(Authentication authentication, @RequestBody Product product) {
    	String response = is.updateProduct(authentication, product);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping(path = "api/updateCombo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCombo(Authentication authentication, @RequestBody Combo combo) {
    	String response = is.updateCombo(authentication, combo);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping(path = "api/manualMovement/{warehouseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> manualMovement(Authentication authentication, @PathVariable("warehouseId") Long warehouseId,
    		@RequestBody List<Map<String, Object>> manualMovement) {
    	is.manualMovement(authentication, warehouseId, manualMovement);
    	return ResponseEntity.ok("Ok");
    }
    
    @GetMapping(path = "api/getProduct")
    public Product getProduct(Authentication authentication, @RequestParam("id") Long id) {
        return is.getProduct(authentication, id);
    }
    
    @GetMapping(path = "api/getProducts")
    public List<Product> getProducts(Authentication authentication) {
        return is.getProducts(authentication);
    }
    
    @GetMapping(path = "api/getCombos")
    public List<Map<String, Object>> getCombos(Authentication authentication) {
        return is.getCombos(authentication);
    }
    
    @GetMapping(path = "api/getCombo")
    public Map<String, Object> getCombo(Authentication authentication, @RequestParam("id") Long id) {
        return is.getCombo(authentication, id);
    }
    
    @GetMapping(path = "api/getInventory")
    public List<Map<String, Object>> getInventory(
    		Authentication authentication,
    		@RequestParam("filterDate") String filterDate,
            @RequestParam("warehouseId") Long warehouseId) {
        return is.getInventory(authentication, filterDate, warehouseId);
    }
    
    @GetMapping(path = "api/getWarehouse")
    public List<Warehouse> getWarehouse(Authentication authentication) {
        return is.getWarehouse(authentication);
    }
    
    @GetMapping(path = "api/getLogs")
    public List<Log> getRegister(Authentication authentication) {
        return is.getLogs(authentication);
    }
    
    @GetMapping(path = "api/getInventoryMovement")
    public List<InventoryMovement> getInventoryMovement(Authentication authentication) {
        return is.getInventoryMovement(authentication);
    }
    
    @PostMapping(path = "api/uploadFile")
    public Map<String, Integer> uploadFile(Authentication authentication, @RequestParam("fileId") String fileId,
    		@RequestParam("warehouseId") Long warehouseId, @RequestParam("file") MultipartFile mpf) {
    	return is.uploadFile(authentication, fileId, warehouseId, mpf);
    }

}
