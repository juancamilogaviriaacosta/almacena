package com.almacenaws.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.almacenaws.model.InventoryMovement;
import com.almacenaws.model.Product;


@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> getProducts() {
        return jdbcTemplate.query("SELECT * FROM product",
            (rs, rowNum) -> {
            	Product tmp = new Product();
            	tmp.setId(rs.getInt("id"));
            	tmp.setSku(rs.getString("sku"));
            	tmp.setName(rs.getString("name"));
            	tmp.setDescription(rs.getString("description"));
                tmp.setCategory(rs.getString("category"));
                return tmp;
            });
    }

    public void createProduct(Product tmp) {
        jdbcTemplate.update("INSERT INTO product(sku, name, description, category) VALUES (?, ?, ?, ?)",
            tmp.getSku(), tmp.getName(), tmp.getDescription(), tmp.getCategory());
    }
    
    public List<InventoryMovement> getInventoryMovement() {
    	List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("SELECT * FROM inventory_movement");
    	System.out.println(Arrays.deepToString(queryForList.toArray()));
    	return null;
    }
}