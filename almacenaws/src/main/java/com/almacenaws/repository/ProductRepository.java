package com.almacenaws.repository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.almacenaws.model.InventoryMovement;
import com.almacenaws.model.MovementType;
import com.almacenaws.model.Product;
import com.almacenaws.model.Usuario;
import com.almacenaws.model.Warehouse;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void initDatabase() {
    	OffsetDateTime now = OffsetDateTime.now();
    	
    	jdbcTemplate.update("INSERT INTO product(sku, name, aux1, aux2, category) VALUES (?, ?, ?, ?, ?)",
    			"001", "Peine Para Mascota", "MCO1854513980", "535353", "Mascotas");
    	jdbcTemplate.update("INSERT INTO product(sku, name, aux1, aux2, category) VALUES (?, ?, ?, ?, ?)",
    			"002", "Kit Organizadores De Maleta", "MCO1388163341", "616161", "Hogar");
    	jdbcTemplate.update("INSERT INTO warehouse (name) VALUES (?)",
    			"Fontibon");
    	jdbcTemplate.update("INSERT INTO warehouse (name) VALUES (?)",
    			"Centro");
    	jdbcTemplate.update("INSERT INTO usuario (email, name, password, role, user_name) VALUES (?, ?, ?, ?, ?)",
    			"cjimportacionesco@gmail.com", "Juan David", "DQ2R789Q234", "Admin", "cjimportacionesco");
    	jdbcTemplate.update("INSERT INTO usuario (email, name, password, role, user_name) VALUES (?, ?, ?, ?, ?)",
    			"usuariocj@gmail.com", "Cosmo", "DQ2R789Q234", "Operador", "cosmo");
    	jdbcTemplate.update("INSERT INTO inventory (product_id, quantity, warehouse_id) VALUES (?, ?, ?)",
    			1, 700, 1);
    	jdbcTemplate.update("INSERT INTO inventory (product_id, quantity, warehouse_id) VALUES (?, ?, ?)",
    			1, 100, 2);
    	jdbcTemplate.update("INSERT INTO inventory (product_id, quantity, warehouse_id) VALUES (?, ?, ?)",
    			2, 500, 1);    	
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
            	tmp.setAux1(rs.getString("aux1"));
                tmp.setCategory(rs.getString("category"));
                return tmp;
            });
    }

    public void createProduct(Product tmp) {
        jdbcTemplate.update("INSERT INTO product(sku, name, aux1, aux2, category) VALUES (?, ?, ?, ?)",
            tmp.getSku(), tmp.getName(), tmp.getAux1(), tmp.getAux2(), tmp.getCategory());
    }
    
    public List<Map<String, Object>> getInventory() {
    	String sql = "SELECT pro.sku, pro.aux1, pro.aux2, pro.name, pro.category, inv.quantity, war.name as warehouse\n"
    			+ "FROM product pro\n"
    			+ "LEFT JOIN inventory inv ON inv.product_id = pro.id\n"
    			+ "LEFT JOIN warehouse war ON inv.warehouse_id = war.id\n"
    			+ "ORDER BY pro.name";
    	List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
    	return queryForList;
	}
    
    public List<Map<String, Object>> getWarehouse() {
    	String sql = "SELECT * FROM warehouse ORDER BY id";
    	List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
    	return queryForList;
	}
    
    public List<Map<String, Object>> getRegister() {
    	String sql = "SELECT * FROM register ORDER BY fechahora";
    	List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
    	return queryForList;
	}
    
    public List<InventoryMovement> getInventoryMovement() {    	
    	return jdbcTemplate.query("SELECT * FROM inventory_movement",
                (rs, rowNum) -> {
                	InventoryMovement tmp = new InventoryMovement();
                	tmp.setId(rs.getInt("id"));
                	tmp.setProduct(new Product(rs.getInt("product_id")));
                	tmp.setFromWarehouse(new Warehouse(rs.getInt("from_warehouse_id")));
                	tmp.setToWarehouse(new Warehouse(rs.getInt("to_warehouse_id")));
                	tmp.setQuantity(rs.getInt("quantity"));
                	tmp.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
                	tmp.setFechahora(rs.getObject("fechahora", OffsetDateTime.class));
                	tmp.setNotes(rs.getString("notes"));
                	tmp.setUsuario(new Usuario(rs.getInt("usuario_id")));
                    return tmp;
                });
    }

	public Map<String, Integer> uploadFile(String fileId, Integer warehouseId, MultipartFile mpf) {
		Map<String, Integer> response = new HashMap<>();
		response.put("success", 0);
		response.put("errors", 0);
		try (Workbook workbook = WorkbookFactory.create(mpf.getInputStream())){
			Map<String, Double> map = new HashMap<>();
			OffsetDateTime now = OffsetDateTime.now();
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
		    while (rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        try {
		        	String code = row.getCell(16).getStringCellValue();
					Double quantity = row.getCell(6).getNumericCellValue();
		        	if(code.startsWith("MCO")) {
		        		if(map.get(code) == null) {
		        			map.put(code, 0D);
		        		}
		        		map.put(code, map.get(code) + quantity);
		        	}
				} catch (Exception e) {
					//e.printStackTrace();
				}
		    }

		    String sqlInv = "INSERT INTO inventory_movement(fechahora, movement_type, notes, quantity, from_warehouse_id, usuario_id, product_id)\n"
		    		+ "VALUES (?, ?, ?, ?, ?, ?, (SELECT id FROM product WHERE aux1 = ?))";
		    String sqlLog = "INSERT INTO register(fechahora, information) VALUES (?, ?)";
		    String sqlSub = "UPDATE inventory inv\n"
		    		+ "SET quantity = inv.quantity - ?\n"
		    		+ "FROM product pro\n"
		    		+ "WHERE inv.product_id = pro.id\n"
		    		+ "AND pro.aux1 = ?\n"
		    		+ "AND inv.warehouse_id = ?";
		    		    
		    for (Map.Entry<String, Double> entry : map.entrySet()) {
		    	String aux1 = entry.getKey();
		    	Double quantity = entry.getValue();
		    	try {
		    		jdbcTemplate.update(sqlInv, now, MovementType.Venta.name(), mpf.getOriginalFilename(), quantity, warehouseId, 1, aux1);
		    		jdbcTemplate.update(sqlSub, quantity, aux1, warehouseId);
		    		response.put("success", response.get("success") + 1);
				} catch (Exception e) {
					jdbcTemplate.update(sqlLog, now, e.getMessage());
					response.put("errors", response.get("errors") + 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}