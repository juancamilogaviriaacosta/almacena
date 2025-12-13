package com.almacenaws.repository;

import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
import com.almacenaws.model.ProductDetail;
import com.almacenaws.model.Status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void initDatabase() {
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
    		if(product.getId()==null) {
    			String newProduct = "INSERT INTO product(category, name, price, sku, status) VALUES (?, ?, ?, ?, ?) RETURNING id";
    			List<Map<String, Object>> newBd = jdbcTemplate.queryForList(newProduct, product.getCategory(), product.getName(),
    					product.getPrice(), product.getSku(), product.getStatus().name());
    			product.setId((Integer) newBd.get(0).get("id"));
    		} else {
	    		String sqlProduct = "UPDATE product SET category=?, name=?, sku=?, status=?, price=? WHERE id = ?";
	    		String deleteCodes = "DELETE FROM code WHERE product_id = ?";	    	
		    	jdbcTemplate.update(sqlProduct, product.getCategory(), product.getName(), product.getSku(),
		    			product.getStatus().name(), product.getPrice(), product.getId());
		    	jdbcTemplate.update(deleteCodes, product.getId());
    		}
    		String newCodes = "INSERT INTO code(code, status, product_id) VALUES (?, ?, ?)";
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
    			String sqlCombo = "UPDATE combo SET name=?, status=? WHERE id = ?";
        		String deleteCodes = "DELETE FROM code WHERE combo_id = ?";
        		String deleteComboProductDetail = "DELETE FROM combo_product_detail WHERE combo_id = ?";
        		String deleteProductDetail = "DELETE FROM product_detail pd\n"
        				+ "WHERE NOT EXISTS (\n"
        				+ "    SELECT 1\n"
        				+ "    FROM combo_product_detail cpd\n"
        				+ "    WHERE cpd.product_detail_id = pd.id\n"
        				+ ")";
        		jdbcTemplate.update(sqlCombo, combo.getName(), combo.getStatus().name(), combo.getId());
    	    	jdbcTemplate.update(deleteCodes, combo.getId());    	    	
    	    	jdbcTemplate.update(deleteComboProductDetail, combo.getId());
    	    	jdbcTemplate.update(deleteProductDetail);
    		}
    		String newCodes = "INSERT INTO code(code, status, combo_id) VALUES (?, ?, ?)";    		
    		String createProductDetail = "INSERT INTO product_detail(quantity, product_id) VALUES (?, ?) RETURNING ID";
    		String createcomboProductDetail = "INSERT INTO combo_product_detail(combo_id, product_detail_id) VALUES (?, ?)";
	    	for (String tmp : codes) {
	    		jdbcTemplate.update(newCodes, tmp, Status.Activo.name(), combo.getId());
			}
	    	if(combo.getProductDetail() != null) {
	    		for (ProductDetail tmp : combo.getProductDetail()) {
	    			List<Integer> pdId = jdbcTemplate.queryForList(createProductDetail, Integer.class, tmp.getQuantity(), tmp.getProduct().getId());
	    			jdbcTemplate.update(createcomboProductDetail, combo.getId(), pdId.get(0));
				}
	    	}
	    	response = "Ok";
    	}
    	return response;
    }
    
    public void manualMovement(Integer warehouseId, List<Map<String, Object>> manualMovement) {
    	OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
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
    	String sql = "SELECT pro.id, pro.category, pro.name, pro.sku, pro.status, pro.price,\n"
    			+ "'[' || STRING_AGG('{\"id\":' || cod.id || ', \"code\":\"' || cod.code || '\"}', ',') || ']' AS code\n"
    			+ "FROM product pro\n"
    			+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
    			+ "WHERE pro.id = ?\n"
    			+ "GROUP BY 1, 2, 3, 4, 5, 6\n"
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
    	String sql = "SELECT pro.id, pro.category, pro.name, pro.sku, pro.status, pro.price, STRING_AGG(DISTINCT cod.code, ';' ORDER BY cod.code) AS codes\n"
    			+ "FROM product pro\n"
    			+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
    			+ "GROUP BY 1, 2, 3, 4, 5, 6\n"
    			+ "ORDER BY 3";
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
        return queryForList;
    }
    
    public List<Map<String, Object>> getCombos() {
    	String sql = "SELECT com.id, com.name, com.status, STRING_AGG(DISTINCT cod.code, ';' ORDER BY cod.code) AS codes\n"
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
    			+ "'[' || STRING_AGG(DISTINCT '{\"product\":{\"id\":' || pro.id || ', \"name\":\"' || pro.name || '\"}' || ', \"quantity\":' || prd.quantity || '}', ',') || ']' AS productDetail\n"
    			+ "FROM combo com\n"
    			+ "LEFT JOIN code cod ON com.id = cod.combo_id\n"
    			+ "LEFT JOIN combo_product_detail cpd ON com.id = cpd.combo_id\n"
    			+ "LEFT JOIN product_detail prd ON cpd.product_detail_id = prd.id\n"
    			+ "LEFT JOIN product pro ON prd.product_id = pro.id\n"
    			+ "WHERE com.id = ?\n"
    			+ "GROUP BY 1, 2, 3\n"
    			+ "ORDER BY 1";
        Map<String, Object> queryForList = jdbcTemplate.queryForList(sql, id).get(0);
        String code = (String) queryForList.get("code");
        String productDetail = (String) queryForList.get("productDetail");
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
        	
        	if(productDetail != null) {
        		List<Map<String, Object>> json = new ObjectMapper().readValue(productDetail, new TypeReference<List<Map<String, Object>>>() {});
            	queryForList.put("productDetail", json);        	
        	} else {
        		List<Map<String, Object>> productsLists = new ArrayList<>();
        		queryForList.put("productDetail", productsLists);
        	}        	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return queryForList;
    }
    
    public List<Map<String, Object>> getInventory(String filterDate, Integer warehouseId) {
    	String sql = "SELECT sub.product_id, pro.name, war.name AS warehouse, inv.quantity, pro.price, to_char(sub.fechahora, 'YYYY-MM-DD HH24:MI:SS') AS fechahora, STRING_AGG(DISTINCT cod.code, ';' ORDER BY cod.code) AS codes\n"
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
    
    public Integer getDbId(List<Map<String, Object>> allProducts, String code) {
    	for (Map<String, Object> map : allProducts) {
			if(code.equals(map.get("code"))) {
				return (Integer) map.get("id");
			}
		}
    	return null;
    }

	public Map<String, Integer> uploadFile(String fileId, Integer warehouseId, MultipartFile mpf) {
		Map<String, Integer> response = new HashMap<>();
		response.put("success", 0);
		response.put("errors", 0);
		
		String sqlAllProducts = "SELECT cod.code, pro.id\n"
				+ "FROM product pro\n"
				+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
				+ "ORDER BY 2";
    	List<Map<String, Object>> allProducts = jdbcTemplate.queryForList(sqlAllProducts);

		try (Workbook workbook = WorkbookFactory.create(mpf.getInputStream())) {
			Map<String, Object[]> map = new HashMap<>();
			OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			DecimalFormat df = new DecimalFormat("#");
		    while (rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        try {
		        	if("ml".equals(fileId)) {
		        		String code = row.getCell(16).getStringCellValue();
		        		Integer id = getDbId(allProducts, code);
			        	String name = row.getCell(17).getStringCellValue();
			        	Integer price = ((Double) row.getCell(19).getNumericCellValue()).intValue();
						Integer quantity = ((Double) row.getCell(6).getNumericCellValue()).intValue();
			        	if(code.startsWith("MCO")) {
			        		if(map.get(code) == null) {
			        			map.put(code, new Object[] {0, name, price, id});
			        		}
			        		map.put(code, new Object[] {(Integer) map.get(code)[0] + quantity, name, price, id});
			        	}
		        	} else {
		        		String code = df.format(row.getCell(26).getNumericCellValue());
		        		Integer id = getDbId(allProducts, code);
			        	String name = row.getCell(29).getStringCellValue();
						Integer price = ((Double) row.getCell(24).getNumericCellValue()).intValue();
						Integer quantity = ((Double) row.getCell(31).getNumericCellValue()).intValue();
						//FIXME
			        	//if(code.startsWith("MCO")) {
			        		if(map.get(code) == null) {
			        			map.put(code, new Object[] {0, name, price, id});
			        		}
			        		map.put(code, new Object[] {(Integer) map.get(code)[0] + quantity, name, price, id});
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
			    		jdbcTemplate.update(sqlNewCode, excelCode, Status.Activo.name(), newProduct.get(0).get("id"));
						jdbcTemplate.update(sqlNewInventory, 0, newProduct.get(0).get("id"), warehouseId, now);
						map.get(excelCode)[3] = newProduct.get(0).get("id");
		    	    }
		    	}
			}
		    
		    if(!comboCodes.isEmpty()) {
			    String comboPlaceholders = String.join(",", Collections.nCopies(comboCodes.size(), "?"));
			    String sqlComboToProducts = "SELECT DISTINCT ccd.code AS cmb_code, pro.id AS pro_id, prd.quantity\n"
			    		+ "FROM combo cmb\n"
			    		+ "INNER JOIN code ccd ON ccd.combo_id = cmb.id\n"
			    		+ "INNER JOIN combo_product_detail cpd ON cmb.id = cpd.combo_id\n"
			    		+ "INNER JOIN product_detail prd ON cpd.product_detail_id = prd.id\n"
			    		+ "INNER JOIN product pro ON prd.product_id = pro.id\n"
			    		+ "WHERE 1 = 1\n"
			    		+ "AND ccd.code IN (" + comboPlaceholders + ")\n";
			    List<Map<String, Object>> comboToProducts = jdbcTemplate.queryForList(sqlComboToProducts, comboCodes.toArray());
			    for (int i = 0; i < comboToProducts.size(); i++) {
			    	Map<String, Object> tmp = comboToProducts.get(i);
					String cmbCode = (String) tmp.get("cmb_code");
					Integer proId = (Integer) tmp.get("pro_id");
					Integer productQuantity = (Integer) tmp.get("quantity");
					Integer cmbQuantity = (Integer) map.get(cmbCode)[0];
					map.put("combo " + i, new Object[] {cmbQuantity * productQuantity, "name", "price", proId});
				}
			    for (String code : comboCodes) {
			    	map.remove(code);
				}
		    }
		    
		    placeholders = String.join(",", Collections.nCopies(map.keySet().size(), "?"));
		    String sqlValidation = "SELECT pro.id, inv.quantity\n"
		    		+ "FROM product pro\n"
		    		+ "LEFT JOIN code cod ON pro.id = cod.product_id\n"
		    		+ "LEFT JOIN inventory inv ON (inv.product_id = pro.id AND inv.warehouse_id = ?)\n"
		    		+ "WHERE inv.quantity IS NULL\n"
		    		+ "AND pro.id IN (" + placeholders + ")";		    
		    String SqlInvMovement = "INSERT INTO inventory_movement(fechahora, movement_type, notes, quantity, warehouse_id, usuario_id, product_id)\n"
		    		+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		    String sqlSubtraction = "UPDATE inventory inv\n"
		    		+ "SET quantity = inv.quantity - ?\n"
		    		+ "FROM product pro\n"
		    		+ "WHERE inv.product_id = pro.id\n"
		    		+ "AND pro.id = ?\n"
		    		+ "AND inv.warehouse_id = ?";
		    String sqlLog = "INSERT INTO register(fechahora, information) VALUES (?, ?)";
		    		    
		    List<Integer> params = new ArrayList<>(map.size() + 1);
		    params.add(warehouseId);
		    for (Object[] val : map.values()) {
		    	params.add((Integer) val[3]);
		    }
		    List<Map<String, Object>> inventory = jdbcTemplate.query(sqlValidation, new ColumnMapRowMapper(), params.toArray());
		    for (Map<String, Object> tmp : inventory) {
				jdbcTemplate.update(sqlNewInventory, 0, tmp.get("id"), warehouseId, now);
			}		    
		    
		    for (Map.Entry<String, Object[]> entry : map.entrySet()) {
		    	String excelCode = entry.getKey();
		    	Integer excelQuantity = (Integer) entry.getValue()[0];
		    	Integer productId = (Integer) entry.getValue()[3];
		    	if(!comboCodes.contains(excelCode)) { 
			    	try {
			    		jdbcTemplate.update(SqlInvMovement, now, MovementType.Venta.name(), mpf.getOriginalFilename(), excelQuantity, warehouseId, 1, productId);
			    		jdbcTemplate.update(sqlSubtraction, excelQuantity, productId, warehouseId);
			    		response.put("success", response.get("success") + 1);
					} catch (Exception e) {
						jdbcTemplate.update(sqlLog, now, e.getMessage());
						response.put("errors", response.get("errors") + 1);
					}
		    	}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}