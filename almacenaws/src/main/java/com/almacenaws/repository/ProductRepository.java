package com.almacenaws.repository;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.almacenaws.model.InventoryMovement;
import com.almacenaws.model.MovementType;
import com.almacenaws.model.Product;
import com.almacenaws.model.Status;
import com.almacenaws.model.Usuario;
import com.almacenaws.model.Warehouse;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void initDatabase() {
    	OffsetDateTime now = OffsetDateTime.now();
    	
    	jdbcTemplate.update("INSERT INTO product(sku, name, category, status) VALUES (?, ?, ?, ?)",
    			"001", "Peine Para Mascota", "Mascotas", "Activo");
    	jdbcTemplate.update("INSERT INTO product(sku, name, category, status) VALUES (?, ?, ?, ?)",
    			"002", "Kit Organizadores De Maleta", "Hogar", "Activo");
    	jdbcTemplate.update("INSERT INTO product(sku, name, category, status) VALUES (?, ?, ?, ?)",
    			"003", "Cepillo automatico a vapor", "Hogar", "Activo");
    	jdbcTemplate.update("INSERT INTO code(code, status, product_id) VALUES (?, ?, ?)",
    			"MCO2024642932", "Activo", 1);
    	jdbcTemplate.update("INSERT INTO code(code, status, product_id) VALUES (?, ?, ?)",
    			"MCO1388163341", "Activo", 2);
    	jdbcTemplate.update("INSERT INTO code(code, status, product_id) VALUES (?, ?, ?)",
    			"MCO1388163342", "Activo", 2);
    	jdbcTemplate.update("INSERT INTO code(code, status, product_id) VALUES (?, ?, ?)",
    			"MCO2813643866", "Activo", 3);    	
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

    public List<Map<String, Object>> getProducts() {
    	String sql = "SELECT pro.id, pro.category, pro.name, pro.sku, pro.status, STRING_AGG(cod.code, ';') AS codes\n"
    			+ "FROM product pro\n"
    			+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
    			+ "GROUP BY 1, 2, 3, 4, 5\n"
    			+ "ORDER BY 1";
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
        return queryForList;
    }
    
    public List<Map<String, Object>> getInventory() {
    	String sql = "SELECT pro.sku, pro.name, pro.category, inv.quantity, war.name as warehouse, STRING_AGG(cod.code, ';') AS codes\n"
    			+ "FROM product pro\n"
    			+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
    			+ "LEFT JOIN inventory inv ON inv.product_id = pro.id\n"
    			+ "LEFT JOIN warehouse war ON inv.warehouse_id = war.id\n"
    			+ "GROUP BY 1, 2, 3, 4, 5\n"
    			+ "ORDER BY 2";
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
			Map<String, Object[]> map = new HashMap<>();
			OffsetDateTime now = OffsetDateTime.now();
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
		    while (rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        try {
		        	String code = row.getCell(16).getStringCellValue();
		        	String name = row.getCell(17).getStringCellValue();
					Integer quantity = ((Double) row.getCell(6).getNumericCellValue()).intValue();
		        	if(code.startsWith("MCO")) {
		        		if(map.get(code) == null) {
		        			map.put(code, new Object[] {0, name});
		        		}
		        		map.put(code, new Object[] {(Integer) map.get(code)[0] + quantity, name});
		        	}
				} catch (Exception e) {
					//e.printStackTrace();
				}
		    }
		    
		    String placeholders = String.join(",", Collections.nCopies(map.keySet().size(), "?"));
		    String sqlProducts = "SELECT STRING_AGG(cod.code, ';') AS codes\n"
		    		+ "FROM product pro\n"
		    		+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
		    		+ "WHERE cod.code IN (" + placeholders + ")";
		    String sqlNewProduct = "INSERT INTO product(name, status) VALUES (?, ?) RETURNING id";
		    String sqlNewCode = "INSERT INTO code(code, status, product_id) VALUES (?, ?, ?)";
		    String sqlNewInventory = "INSERT INTO inventory(quantity, product_id, warehouse_id) VALUES (?, ?, ?)";
		    String sqlValidation = "SELECT pro.id, inv.quantity\n"
		    		+ "FROM product pro\n"
		    		+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
		    		+ "LEFT JOIN inventory inv ON (inv.product_id = pro.id AND inv.warehouse_id = ?)\n"
		    		+ "WHERE inv.quantity IS NULL\n"
		    		+ "AND cod.code IN (" + placeholders + ")";
		    
		    String SqlInvMovement = "INSERT INTO inventory_movement(fechahora, movement_type, notes, quantity, from_warehouse_id, usuario_id, product_id)\n"
		    		+ "VALUES (?, ?, ?, ?, ?, ?, (SELECT product_id FROM code WHERE code = ? LIMIT 1))";
		    String sqlSubtraction = "UPDATE inventory inv\n"
		    		+ "SET quantity = inv.quantity - ?\n"
		    		+ "FROM product pro\n"
		    		+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
		    		+ "WHERE inv.product_id = pro.id\n"
		    		+ "AND cod.code = ?\n"
		    		+ "AND inv.warehouse_id = ?";
		    String sqlLog = "INSERT INTO register(fechahora, information) VALUES (?, ?)";
		    
		    List<Map<String, Object>> products = jdbcTemplate.query(sqlProducts, new ColumnMapRowMapper(), map.keySet().toArray());
		    for (String excelCode : map.keySet()) {
		    	boolean isInDb = false;
		    	for (Map<String, Object> tmp : products) {
					if(((String) tmp.get("codes")).contains(excelCode)) {
						isInDb = true;
					}
				}
		    	if(!isInDb) {
		    		String name = (String) map.get(excelCode)[1];
		    		List<Map<String, Object>> newProduct = jdbcTemplate.queryForList(sqlNewProduct, name, Status.Incompleto.name());
		    		jdbcTemplate.update(sqlNewCode, excelCode, Status.Incompleto.name() ,newProduct.get(0).get("id"));
					jdbcTemplate.update(sqlNewInventory, 0, newProduct.get(0).get("id"), warehouseId);
		    	}
			}
		    
			Object[] array = new Object[map.keySet().size() + 1];
			array[0] = warehouseId;
			System.arraycopy(map.keySet().toArray(), 0, array, 1, map.keySet().size());				    
		    List<Map<String, Object>> inventory = jdbcTemplate.query(sqlValidation, new ColumnMapRowMapper(), array);
		    for (Map<String, Object> tmp : inventory) {
				jdbcTemplate.update(sqlNewInventory, 0, tmp.get("id"), warehouseId);
			}		    
		    
		    for (Map.Entry<String, Object[]> entry : map.entrySet()) {
		    	String excelCode = entry.getKey();
		    	Integer excelQuantity = (Integer) entry.getValue()[0];
		    	try {
		    		jdbcTemplate.update(SqlInvMovement, now, MovementType.Venta.name(), mpf.getOriginalFilename(), excelQuantity, warehouseId, 1, excelCode);
		    		jdbcTemplate.update(sqlSubtraction, excelQuantity, excelCode, warehouseId);
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