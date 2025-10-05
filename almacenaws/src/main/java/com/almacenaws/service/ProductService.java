package com.almacenaws.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.almacenaws.model.InventoryMovement;
import com.almacenaws.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void initDatabase() {
    	productRepository.initDatabase();
    }
    
    public List<Map<String, Object>> getProducts() {
        return productRepository.getProducts();
    }
    
    public List<Map<String, Object>> getInventory () {
    	return productRepository.getInventory();
    }
    
    public List<Map<String, Object>> getWarehouse () {
    	return productRepository.getWarehouse();
    }
    
    public List<Map<String, Object>> getRegister () {
    	return productRepository.getRegister();
    }
    
    public List<InventoryMovement> getInventoryMovement () {
    	return productRepository.getInventoryMovement();
    }

	public Map<String, Integer> uploadFile(String fileId, Integer warehouseId, MultipartFile mpf) {
		return productRepository.uploadFile(fileId, warehouseId, mpf);
	}
}