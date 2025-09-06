package com.almacenaws.repository;

import java.util.Arrays;
import java.util.Date;
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
    
    public void initDatabase() {
    	Date now = new Date();
    	jdbcTemplate.update("INSERT INTO product(sku, name, description, category) VALUES (?, ?, ?, ?)",
    			"001", "Fuente agua para gato", "con sensor de movimiento", "Mascotas");
    	jdbcTemplate.update("INSERT INTO product(sku, name, description, category) VALUES (?, ?, ?, ?)",
    			"002", "Dispensador condimentos", "Set 5 condimenteros", "Hogar");
    	jdbcTemplate.update("INSERT INTO warehouse (name) VALUES (?)",
    			"Fontibon");
    	jdbcTemplate.update("INSERT INTO warehouse (name) VALUES (?)",
    			"Centro");
    	jdbcTemplate.update("INSERT INTO usuario (email, name, password, role, user_name) VALUES (?, ?, ?, ?, ?)",
    			"cjimportacionesco@gmail.com", "Juan David", "DQ2R789Q234", "Admin", "cjimportacionesco");
    	jdbcTemplate.update("INSERT INTO usuario (email, name, password, role, user_name) VALUES (?, ?, ?, ?, ?)",
    			"usuariocj@gmail.com", "Cosmo", "DQ2R789Q234", "Operador", "cosmo");
    	jdbcTemplate.update("INSERT INTO inventory_movement(movement_type, notes, quantity, fechahora, from_warehouse_id, product_id, to_warehouse_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
    			"Entrada", "Llega contenedor 1", 100, now, null, 1, null, 1);
    	jdbcTemplate.update("INSERT INTO inventory_movement(movement_type, notes, quantity, fechahora, from_warehouse_id, product_id, to_warehouse_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
    			"Entrada", "Llega contenedor 2", 120, now, null, 2, null, 1);
    	jdbcTemplate.update("INSERT INTO inventory_movement(movement_type, notes, quantity, fechahora, from_warehouse_id, product_id, to_warehouse_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
    			"Salida", "Venta ML", 30, now, 2, 2, null, 1);
    	jdbcTemplate.update("INSERT INTO inventory_movement(movement_type, notes, quantity, fechahora, from_warehouse_id, product_id, to_warehouse_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
    			"Traslado", "Traslado de F a C", 45, now, 1, 1, 2, 1);
    	
    }

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