package com.almacenaws.repository;

import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.almacenaws.model.Code;
import com.almacenaws.model.Combo;
import com.almacenaws.model.InventoryMovement;
import com.almacenaws.model.MovementType;
import com.almacenaws.model.Product;
import com.almacenaws.model.Status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void initDatabase() {    	
    	/*OffsetDateTime now = OffsetDateTime.now();
    	
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
    			"MCO2813643866", "Activo", 3);*/    	
    	jdbcTemplate.update("INSERT INTO warehouse (name) VALUES (?)",
    			"San Façon");
    	jdbcTemplate.update("INSERT INTO warehouse (name) VALUES (?)",
    			"T20");
    	jdbcTemplate.update("INSERT INTO warehouse (name) VALUES (?)",
    			"Fontibón");
    	jdbcTemplate.update("INSERT INTO usuario (email, name, password, role, user_name) VALUES (?, ?, ?, ?, ?)",
    			"cjimportacionesco@gmail.com", "Juan David", "DQ2R789Q234", "Admin", "cjimportacionesco");
    	jdbcTemplate.update("INSERT INTO usuario (email, name, password, role, user_name) VALUES (?, ?, ?, ?, ?)",
    			"usuariocj@gmail.com", "Cosmo", "DQ2R789Q234", "Operador", "cosmo");
    	/*jdbcTemplate.update("INSERT INTO inventory (product_id, quantity, warehouse_id) VALUES (?, ?, ?)",
    			1, 700, 1);
    	jdbcTemplate.update("INSERT INTO inventory (product_id, quantity, warehouse_id) VALUES (?, ?, ?)",
    			1, 100, 2);
    	jdbcTemplate.update("INSERT INTO inventory (product_id, quantity, warehouse_id) VALUES (?, ?, ?)",
    			2, 500, 1);    	
    	jdbcTemplate.update("INSERT INTO inventory_movement(movement_type, notes, quantity, fechahora, warehouse_id, product_id, to_warehouse_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
    			"Entrada", "Llega contenedor 1", 100, now, null, 1, null, 1);
    	jdbcTemplate.update("INSERT INTO inventory_movement(movement_type, notes, quantity, fechahora, warehouse_id, product_id, to_warehouse_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
    			"Entrada", "Llega contenedor 2", 120, now, null, 2, null, 1);
    	jdbcTemplate.update("INSERT INTO inventory_movement(movement_type, notes, quantity, fechahora, warehouse_id, product_id, to_warehouse_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
    			"Salida", "Venta ML", 30, now, 2, 2, null, 1);
    	jdbcTemplate.update("INSERT INTO inventory_movement(movement_type, notes, quantity, fechahora, warehouse_id, product_id, to_warehouse_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
    			"Traslado", "Traslado de F a C", 45, now, 1, 1, 2, 1);*/
    	
    }
    
    public String updateProduct(Product product) {
    	String response = "";
    	List<String> codes = product.getCode()
    			.stream()
    			.map(Code::getCode)
    			.filter(Objects::nonNull)
    			.filter(code -> !code.trim().isEmpty()).toList();
    	List<String> names = new ArrayList<>();
    	if(!codes.isEmpty()) {
    		String placeholders = String.join(",", Collections.nCopies(codes.size(), "?"));
        	String sqlCodes = "SELECT DISTINCT pro.name\n"
        			+ "FROM code cod\n"
        			+ "INNER JOIN product pro ON cod.product_id = pro.id\n"
        			+ "WHERE 1 = 1\n"
        			+ "AND cod.code IN ("+ placeholders +")\n"
    				+ "AND pro.id != ?";
        	List<Object> codesToQuery = new ArrayList<>(codes);
        	codesToQuery.add(product.getId());
        	names = jdbcTemplate.queryForList(sqlCodes, String.class, codesToQuery.toArray());
        	if(!names.isEmpty()) {
	        	response = "Código duplicado en:<br/>";
	    		for (String tmp : names) {
	    			response = response + tmp + "<br/>";
	    		}
        	}
    	}
    	
    	if(names.isEmpty()) {
    		String sqlProduct = "UPDATE product SET category=?, name=?, sku=?, status=? WHERE id = ?";
    		String deleteCodes = "DELETE FROM code WHERE product_id = ?";
    		String newCodes = "INSERT INTO code(code, status, product_id) VALUES (?, ?, ?)";
	    	jdbcTemplate.update(sqlProduct, product.getCategory(), product.getName(), product.getSku(), product.getStatus().name(), product.getId());
	    	jdbcTemplate.update(deleteCodes, product.getId());
	    	for (String tmp : codes) {
	    		jdbcTemplate.update(newCodes, tmp, Status.Activo.name(), product.getId());
			}
	    	response = "Ok";
    	}
    	return response;
    }
    
    public String updateCombo(Combo combo) {
    	String response = "";
    	List<String> codes = combo.getCode()
    			.stream()
    			.map(Code::getCode)
    			.filter(Objects::nonNull)
    			.filter(code -> !code.trim().isEmpty()).toList();
    	List<String> names = new ArrayList<>();
    	if(!codes.isEmpty()) {
    		String placeholders = String.join(",", Collections.nCopies(codes.size(), "?"));
        	String sqlCodes = "SELECT DISTINCT com.name\n"
        			+ "FROM code cod\n"
        			+ "INNER JOIN combo com ON cod.combo_id = com.id\n"
        			+ "WHERE 1 = 1\n"
        			+ "AND cod.code IN ("+ placeholders +")\n"
    				+ "AND com.id != ?";
        	List<Object> codesToQuery = new ArrayList<>(codes);
        	codesToQuery.add(combo.getId());
        	names = jdbcTemplate.queryForList(sqlCodes, String.class, codesToQuery.toArray());
        	if(!names.isEmpty()) {
	        	response = "Código duplicado en:<br/>";
	    		for (String tmp : names) {
	    			response = response + tmp + "<br/>";
	    		}
        	}
    	}
    	
    	if(names.isEmpty()) {
    		if(combo.getId() == null) {
    			String newCombo = "INSERT INTO combo(name, status) VALUES (?, ?) RETURNING id";
    			List<Map<String, Object>> newComboBd = jdbcTemplate.queryForList(newCombo, combo.getName(), Status.Activo.name());
    			combo.setId((Integer) newComboBd.get(0).get("id"));
    		} else {
    			String sqlProduct = "UPDATE combo SET name=?, status=? WHERE id = ?";
        		String deleteCodes = "DELETE FROM code WHERE combo_id = ?";
        		String deleteComboProducts = "DELETE FROM combo_product WHERE combo_id = ?";
        		jdbcTemplate.update(sqlProduct, combo.getName(), combo.getStatus().name(), combo.getId());
    	    	jdbcTemplate.update(deleteCodes, combo.getId());    	    	
    	    	jdbcTemplate.update(deleteComboProducts, combo.getId());
    		}
    		String newCodes = "INSERT INTO code(code, status, combo_id) VALUES (?, ?, ?)";    		
    		String createComboProducts = "INSERT INTO combo_product(combo_id, product_id)	VALUES (?, ?)";
	    	for (String tmp : codes) {
	    		jdbcTemplate.update(newCodes, tmp, Status.Activo.name(), combo.getId());
			}
	    	if(combo.getProduct() != null) {
	    		for (Product tmp : combo.getProduct()) {
	    			jdbcTemplate.update(createComboProducts, combo.getId(), tmp.getId());
				}
	    	}
	    	response = "Ok";
    	}
    	return response;
    }
    
    public void manualMovement(Integer warehouseId, List<Map<String, Object>> manualMovement) {
    	OffsetDateTime now = OffsetDateTime.now();
    	String sqlInvMovement = "INSERT INTO inventory_movement(fechahora, movement_type, quantity, warehouse_id, usuario_id, product_id)\n"
	    		+ "VALUES (?, ?, ?, ?, ?, ?)";
    	String sqlAdition = "UPDATE inventory SET quantity = quantity + ? WHERE product_id=? AND warehouse_id=?";
    	for (Map<String, Object> tmp : manualMovement) {
			Integer productId = (Integer) tmp.get("product_id");
			Integer mquantity = (Integer) tmp.get("mquantity");
			if(productId != null && mquantity != null && !mquantity.equals(0)) {
				jdbcTemplate.update(sqlInvMovement, now, MovementType.Manual.name(), mquantity, warehouseId, 1, productId);
				jdbcTemplate.update(sqlAdition, mquantity, productId, warehouseId);
			}
		}
    }


    public Map<String, Object> getProduct(Integer id) {
    	String sql = "SELECT pro.id, pro.category, pro.name, pro.sku, pro.status,\n"
    			+ "'[' || STRING_AGG('{\"id\":' || cod.id || ', \"code\":\"' || cod.code || '\"}', ',') || ']' AS code\n"
    			+ "FROM product pro\n"
    			+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
    			+ "WHERE pro.id = ?\n"
    			+ "GROUP BY 1, 2, 3, 4, 5\n"
    			+ "ORDER BY 1";
        Map<String, Object> queryForList = jdbcTemplate.queryForList(sql, id).get(0);
        String code = (String) queryForList.get("code");
        try {
        	
        	if(code != null) {
        		List<Map<String, Object>> json = new ObjectMapper().readValue(code, new TypeReference<List<Map<String, Object>>>() {});
            	queryForList.put("code", json);        	
        	} else {
        		List<Map<String, Object>> codes = new ArrayList<>();
        		Map<String, Object> blank = new HashMap<>();
				codes.add(blank);
        		queryForList.put("code", codes);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return queryForList;
    }
    
    public List<Map<String, Object>> getProducts() {
    	String sql = "SELECT pro.id, pro.category, pro.name, pro.sku, pro.status, STRING_AGG(cod.code, ';') AS codes\n"
    			+ "FROM product pro\n"
    			+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
    			+ "GROUP BY 1, 2, 3, 4, 5\n"
    			+ "ORDER BY 3";
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
        return queryForList;
    }
    
    public List<Map<String, Object>> getCombos() {
    	String sql = "SELECT com.id, com.name, com.status, STRING_AGG(cod.code, ';') AS codes\n"
    			+ "FROM combo com\n"
    			+ "LEFT JOIN code cod ON com.id = cod.combo_id\n"
    			+ "GROUP BY 1, 2, 3\n"
    			+ "ORDER BY 2";
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
        return queryForList;
    }
    
    public Map<String, Object> getCombo(Integer id) {
    	String sql = "SELECT com.id, com.name, com.status,\n"
    			+ "'[' || STRING_AGG(DISTINCT '{\"id\":' || cod.id || ', \"code\":\"' || cod.code || '\"}', ',') || ']' AS code,\n"
    			+ "'[' || STRING_AGG(DISTINCT '{\"id\":' || pro.id || ', \"name\":\"' || pro.name || '\"}', ',') || ']' AS product\n"
    			+ "FROM combo com\n"
    			+ "LEFT JOIN code cod ON com.id = cod.combo_id\n"
    			+ "LEFT JOIN combo_product cpr ON com.id = cpr.combo_id\n"
    			+ "LEFT JOIN product pro ON cpr.product_id = pro.id\n"
    			+ "WHERE com.id = ?\n"
    			+ "GROUP BY 1, 2, 3\n"
    			+ "ORDER BY 1";
        Map<String, Object> queryForList = jdbcTemplate.queryForList(sql, id).get(0);
        String code = (String) queryForList.get("code");
        String product = (String) queryForList.get("product");
        try {
        	if(code != null) {
        		List<Map<String, Object>> json = new ObjectMapper().readValue(code, new TypeReference<List<Map<String, Object>>>() {});
            	queryForList.put("code", json);        	
        	} else {
        		List<Map<String, Object>> codes = new ArrayList<>();
        		Map<String, Object> blank = new HashMap<>();
				codes.add(blank);
        		queryForList.put("code", codes);
        	}
        	
        	if(product != null) {
        		List<Map<String, Object>> json = new ObjectMapper().readValue(product, new TypeReference<List<Map<String, Object>>>() {});
            	queryForList.put("product", json);        	
        	} else {
        		List<Map<String, Object>> productsLists = new ArrayList<>();
        		queryForList.put("product", productsLists);
        	}        	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return queryForList;
    }
    
    public List<Map<String, Object>> getInventory(String filterDate, Integer warehouseId) {
    	String sql = "SELECT sub.product_id, pro.name, war.name AS warehouse, inv.quantity, pro.price, to_char(sub.fechahora, 'YYYY-MM-DD HH24:MI:SS') AS fechahora, STRING_AGG(cod.code, ';') AS codes\n"
    			+ "FROM (\n"
    			+ "SELECT pro.id AS product_id, MAX(inv.fechahora) AS fechahora\n"
    			+ "FROM product pro\n"
    			+ "LEFT JOIN inventory inv ON inv.product_id = pro.id\n"
    			+ "WHERE inv.fechahora <= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')\n"
    			+ (!Integer.valueOf(-1).equals(warehouseId) ? "AND warehouse_id = ?\n" : "")
    			+ "GROUP BY 1\n"
    			+ ") sub\n"
    			+ "LEFT JOIN inventory inv ON (sub.product_id = inv.product_id AND sub.fechahora = inv.fechahora)\n"
    			+ "LEFT JOIN product pro ON sub.product_id = pro.id\n"
    			+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
    			+ "LEFT JOIN warehouse war ON inv.warehouse_id = war.id\n"
    			+ "GROUP BY 1, 2, 3, 4, 5, 6\n"
    			+ "ORDER BY 2";
    	
    	List<Object> params = new ArrayList<>();
    	params.add(filterDate + " 23:59:59");
    	if (!Integer.valueOf(-1).equals(warehouseId)) {
    	    params.add(warehouseId);
    	}
    	List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, params.toArray());
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
                	//tmp.setProduct(new Product(rs.getInt("product_id")));
                	//tmp.setFromWarehouse(new Warehouse(rs.getInt("warehouse_id")));
                	//tmp.setToWarehouse(new Warehouse(rs.getInt("warehouse_id")));
                	tmp.setQuantity(rs.getInt("quantity"));
                	tmp.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
                	tmp.setFechahora(rs.getObject("fechahora", OffsetDateTime.class));
                	tmp.setNotes(rs.getString("notes"));
                	//tmp.setUsuario(new Usuario(rs.getInt("usuario_id")));
                    return tmp;
                });
    }

	public Map<String, Integer> uploadFile(String fileId, Integer warehouseId, MultipartFile mpf) {
		Map<String, Integer> response = new HashMap<>();
		response.put("success", 0);
		response.put("errors", 0);
		try (Workbook workbook = WorkbookFactory.create(mpf.getInputStream())) {
			Map<String, Object[]> map = new HashMap<>();
			OffsetDateTime now = OffsetDateTime.now();
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			DecimalFormat df = new DecimalFormat("#");
		    while (rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        try {
		        	if("ml".equals(fileId)) {
		        		String code = row.getCell(16).getStringCellValue();
			        	String name = row.getCell(17).getStringCellValue();
			        	Integer price = ((Double) row.getCell(19).getNumericCellValue()).intValue();
						Integer quantity = ((Double) row.getCell(6).getNumericCellValue()).intValue();
			        	if(code.startsWith("MCO")) {
			        		if(map.get(code) == null) {
			        			map.put(code, new Object[] {0, name, price});
			        		}
			        		map.put(code, new Object[] {(Integer) map.get(code)[0] + quantity, name, price});
			        	}
		        	} else {
		        		String code = df.format(row.getCell(26).getNumericCellValue());
			        	String name = row.getCell(29).getStringCellValue();
						Integer price = ((Double) row.getCell(24).getNumericCellValue()).intValue();
						Integer quantity = ((Double) row.getCell(31).getNumericCellValue()).intValue();
						//FIXME
			        	//if(code.startsWith("MCO")) {
			        		if(map.get(code) == null) {
			        			map.put(code, new Object[] {0, name, price});
			        		}
			        		map.put(code, new Object[] {(Integer) map.get(code)[0] + quantity, name, price});
			        	//}
		        	}
				} catch (Exception e) {
					//e.printStackTrace();
				}
		    }
		    
		    String placeholders = String.join(",", Collections.nCopies(map.keySet().size(), "?"));
		    String sqlProducts = "SELECT STRING_AGG(cod.code, ';') AS codes \n"
		    		+ "FROM code cod\n"
		    		+ "WHERE cod.code IN (" + placeholders + ")";
		    String sqlNewProduct = "INSERT INTO product(name, status, price) VALUES (?, ?, ?) RETURNING id";
		    String sqlNewCode = "INSERT INTO code(code, status, product_id) VALUES (?, ?, ?)";
		    String sqlNewCombo = "INSERT INTO combo(name, status) VALUES (?, ?) RETURNING id";
		    String sqlNewComboCode = "INSERT INTO code(code, status, combo_id) VALUES (?, ?, ?)";
		    String sqlNewInventory = "INSERT INTO inventory(quantity, product_id, warehouse_id, fechahora) VALUES (?, ?, ?, ?)";
		    
		    Set<String> comboCodes = new HashSet<>();
		    List<Map<String, Object>> products = jdbcTemplate.query(sqlProducts, new ColumnMapRowMapper(), map.keySet().toArray());
		    for (String excelCode : map.keySet()) {
		    	boolean isInDb = false;
		    	for (Map<String, Object> tmp : products) {
					if(tmp.get("codes") != null && ((String) tmp.get("codes")).contains(excelCode)) {
						isInDb = true;
					}
				}
		    	String name = (String) map.get(excelCode)[1];
		    	Integer price = (Integer) map.get(excelCode)[2];
		    	boolean isCombo = name.contains("&+");
		    	if (isCombo) {
		    	    comboCodes.add(excelCode);
		    	}
		    	if (!isInDb) {
		    	    if (isCombo) {
		    	    	List<Map<String, Object>> newCombo = jdbcTemplate.queryForList(sqlNewCombo, name, Status.Incompleto.name());
			    		jdbcTemplate.update(sqlNewComboCode, excelCode, Status.Activo.name(), newCombo.get(0).get("id"));
		    	    } else {
		    	    	List<Map<String, Object>> newProduct = jdbcTemplate.queryForList(sqlNewProduct, name, Status.Incompleto.name(), price);
			    		jdbcTemplate.update(sqlNewCode, excelCode, Status.Activo.name() ,newProduct.get(0).get("id"));
						jdbcTemplate.update(sqlNewInventory, 0, newProduct.get(0).get("id"), warehouseId, now);
		    	    }
		    	}
			}
		    
		    if(!comboCodes.isEmpty()) {
			    String comboPlaceholders = String.join(",", Collections.nCopies(comboCodes.size(), "?"));
			    String sqlComboToProducts = "SELECT ccd.code AS cmb_code, STRING_AGG(cpr.code, ';') AS pro_codes\n"
			    		+ "FROM combo_product cop\n"
			    		+ "INNER JOIN combo cmb ON cop.combo_id = cmb.id\n"
			    		+ "INNER JOIN code ccd ON ccd.combo_id = cmb.id\n"
			    		+ "INNER JOIN product pro ON cop.product_id = pro.id\n"
			    		+ "INNER JOIN code cpr ON pro.id = cpr.product_id\n"
			    		+ "WHERE 1 = 1\n"
			    		+ "AND ccd.code IN (" + comboPlaceholders + ")\n"
	    				+ "GROUP BY ccd.code";
			    List<Map<String, Object>> comboToProducts = jdbcTemplate.queryForList(sqlComboToProducts, comboCodes.toArray());
			    for (Map<String, Object> tmp : comboToProducts) {
					String cmbCode = (String) tmp.get("cmb_code");
					String[] proCodes = ((String) tmp.get("pro_codes")).split(";");
					Integer cmbQuantity = (Integer) map.get(cmbCode)[0];
					map.remove(cmbCode);
					for (String proCode : proCodes) {
						Integer prodQuantity = map.get(proCode)[0] == null ? 0 : (Integer) map.get(proCode)[0];
						map.put(proCode, new Object[] {cmbQuantity + prodQuantity, null});
					}
				}
		    }
		    
		    placeholders = String.join(",", Collections.nCopies(map.keySet().size(), "?"));		    
		    String sqlValidation = "SELECT pro.id, inv.quantity\n"
		    		+ "FROM product pro\n"
		    		+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
		    		+ "LEFT JOIN inventory inv ON (inv.product_id = pro.id AND inv.warehouse_id = ?)\n"
		    		+ "WHERE inv.quantity IS NULL\n"
		    		+ "AND cod.code IN (" + placeholders + ")";
		    String SqlInvMovement = "INSERT INTO inventory_movement(fechahora, movement_type, notes, quantity, warehouse_id, usuario_id, product_id)\n"
		    		+ "VALUES (?, ?, ?, ?, ?, ?, (SELECT product_id FROM code WHERE code = ? LIMIT 1))";
		    String sqlSubtraction = "UPDATE inventory inv\n"
		    		+ "SET quantity = inv.quantity - ?\n"
		    		+ "FROM product pro\n"
		    		+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
		    		+ "WHERE inv.product_id = pro.id\n"
		    		+ "AND cod.code = ?\n"
		    		+ "AND inv.warehouse_id = ?";
		    String sqlLog = "INSERT INTO register(fechahora, information) VALUES (?, ?)";
		    
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