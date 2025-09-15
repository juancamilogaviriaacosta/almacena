package com.almacenaws.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.almacenaws.model.InventoryMovement;
import com.almacenaws.model.Product;
import com.almacenaws.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void initDatabase() {
    	productRepository.initDatabase();
    }
    
    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    public void createProduct(Product product) {
    	productRepository.createProduct(product);
    }
    
    public List<Map<String, Object>> getInventory () {
    	return productRepository.getInventory();
    }
    
    public List<InventoryMovement> getInventoryMovement () {
    	return productRepository.getInventoryMovement();
    }

	public void uploadFile(String id, MultipartFile mpf) {
		productRepository.uploadFile(id, mpf);
	}
}