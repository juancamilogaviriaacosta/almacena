package co.com.almacena.services;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Instant;
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
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.almacena.entities.Code;
import co.com.almacena.entities.Combo;
import co.com.almacena.entities.Inventory;
import co.com.almacena.entities.InventoryMovement;
import co.com.almacena.entities.Log;
import co.com.almacena.entities.MovementType;
import co.com.almacena.entities.Product;
import co.com.almacena.entities.ProductDetail;
import co.com.almacena.entities.Role;
import co.com.almacena.entities.Status;
import co.com.almacena.entities.Tenant;
import co.com.almacena.entities.User;
import co.com.almacena.entities.Warehouse;
import co.com.almacena.repositories.CodeRepository;
import co.com.almacena.repositories.ComboRepository;
import co.com.almacena.repositories.InventoryMovementRepository;
import co.com.almacena.repositories.InventoryRepository;
import co.com.almacena.repositories.LogRepository;
import co.com.almacena.repositories.ProductRepository;
import co.com.almacena.repositories.TenantRepository;
import co.com.almacena.repositories.UserRepository;
import co.com.almacena.repositories.WarehouseRepository;
import co.com.almacena.security.UserDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
@SuppressWarnings("unchecked")
public class InventoryService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	private CodeRepository codr;
	
	@Autowired
	private ComboRepository comr;
	
	@Autowired
	private InventoryRepository ir;
	
	@Autowired
	private InventoryMovementRepository imr;
	
	@Autowired
	private LogRepository lr;
	
	@Autowired
	private ProductRepository pr;
	
	@Autowired
	private TenantRepository tr;
	
	@Autowired
	private UserRepository ur;
	
	@Autowired
	private WarehouseRepository wr;
	    
    public void initDatabase() {
    	String enc = new BCryptPasswordEncoder().encode("123456");
    	Tenant t1 = tr.save(new Tenant(null, "CJ Importaciones CO", "Basic"));
    	wr.save(new Warehouse(null, t1, "San Façon"));
    	wr.save(new Warehouse(null, t1, "T20"));
    	wr.save(new Warehouse(null, t1, "Fontibón"));
    	ur.save(new User(null, t1, "cjimportacionesco", "Juan David", "cjimportacionesco@gmail.com", Role.Admin, enc));
    	ur.save(new User(null, t1, "usuario1cj1co", "Usuario 1", "usuario1cj1co@gmail.com", Role.User, enc));
    }
    
    public String updateProduct(Authentication authentication, Product product) {
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
    			product.setId((Long) newBd.get(0).get("id"));
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
    
    public String updateCombo(Authentication authentication, Combo combo) {
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
    			combo.setId((Long) newComboBd.get(0).get("id"));
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
	    			List<Long> pdId = jdbcTemplate.queryForList(createProductDetail, Long.class, tmp.getQuantity(), tmp.getProduct().getId());
	    			jdbcTemplate.update(createcomboProductDetail, combo.getId(), pdId.get(0));
				}
	    	}
	    	response = "Ok";
    	}
    	return response;
    }
    
    public void manualMovement(Authentication authentication, Long warehouseId, List<Map<String, Object>> manualMovement) {
    	OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
    	String sqlInvMovement = "INSERT INTO inventory_movement(fechahora, movement_type, quantity, warehouse_id, usuario_id, product_id)\n"
	    		+ "VALUES (?, ?, ?, ?, ?, ?)";
    	String sqlAdition = "UPDATE inventory SET quantity = quantity + ? WHERE product_id=? AND warehouse_id=?";
    	for (Map<String, Object> tmp : manualMovement) {
			Long productId = (Long) tmp.get("product_id");
			Integer mquantity = (Integer) tmp.get("mquantity");
			if(productId != null && mquantity != null && !mquantity.equals(0)) {
				jdbcTemplate.update(sqlInvMovement, now, MovementType.Manual.name(), mquantity, warehouseId, 1, productId);
				jdbcTemplate.update(sqlAdition, mquantity, productId, warehouseId);
			}
		}
    }


    public Map<String, Object> getProduct(Authentication authentication, Long id) {
    	String sql = "SELECT pro.id, pro.category, pro.name, pro.sku, pro.status, pro.price,\n"
    			+ "'[' || STRING_AGG('{\"id\":' || cod.id || ', \"code\":\"' || cod.code || '\"}', ',') || ']' AS code\n"
    			+ "FROM products pro\n"
    			+ "LEFT JOIN codes cod ON pro.id = cod.product_id\n"
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
    
    public List<Map<String, Object>> getProducts(Authentication authentication) {
    	String sql = "SELECT pro.id, pro.category, pro.name, pro.sku, pro.status, pro.price, STRING_AGG(DISTINCT cod.code, ';' ORDER BY cod.code) AS codes\n"
    			+ "FROM products pro\n"
    			+ "LEFT JOIN codes cod ON pro.id = cod.product_id\n"
    			+ "GROUP BY 1, 2, 3, 4, 5, 6\n"
    			+ "ORDER BY 3";
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
        return queryForList;
    }
    
    public List<Map<String, Object>> getCombos(Authentication authentication) {
    	String sql = "SELECT com.id, com.name, com.status, STRING_AGG(DISTINCT cod.code, ';' ORDER BY cod.code) AS codes\n"
    			+ "FROM combos com\n"
    			+ "LEFT JOIN codes cod ON com.id = cod.combo_id\n"
    			+ "GROUP BY 1, 2, 3\n"
    			+ "ORDER BY 2";
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
        return queryForList;
    }
    
    public Map<String, Object> getCombo(Authentication authentication, Long id) {
    	String sql = "SELECT com.id, com.name, com.status,\n"
    			+ "'[' || STRING_AGG(DISTINCT '{\"id\":' || cod.id || ', \"code\":\"' || cod.code || '\"}', ',') || ']' AS code,\n"
    			+ "'[' || STRING_AGG(DISTINCT '{\"product\":{\"id\":' || pro.id || ', \"name\":\"' || pro.name || '\"}' || ', \"quantity\":' || prd.quantity || '}', ',') || ']' AS productDetail\n"
    			+ "FROM combos com\n"
    			+ "LEFT JOIN codes cod ON com.id = cod.combo_id\n"
    			+ "LEFT JOIN combos_product_detail cpd ON com.id = cpd.combo_id\n"
    			+ "LEFT JOIN product_details prd ON cpd.product_detail_id = prd.id\n"
    			+ "LEFT JOIN products pro ON prd.product_id = pro.id\n"
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
    
    public List<Map<String, Object>> getInventory(Authentication authentication, String filterDate, Long warehouseId) {
    	String sql = "SELECT sub.product_id, pro.name, war.name AS warehouse, inv.quantity, pro.price, to_char(sub.fechahora, 'YYYY-MM-DD HH24:MI:SS') AS fechahora, STRING_AGG(DISTINCT cod.code, ';' ORDER BY cod.code) AS codes\n"
    			+ "FROM (\n"
    			+ "SELECT pro.id AS product_id, MAX(inv.fechahora) AS fechahora\n"
    			+ "FROM products pro\n"
    			+ "LEFT JOIN inventories inv ON inv.product_id = pro.id\n"
    			+ "WHERE inv.fechahora <= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')\n"
    			+ (!Long.valueOf(-1).equals(warehouseId) ? "AND warehouse_id = ?\n" : "")
    			+ "GROUP BY 1\n"
    			+ ") sub\n"
    			+ "LEFT JOIN inventories inv ON (sub.product_id = inv.product_id AND sub.fechahora = inv.fechahora)\n"
    			+ "LEFT JOIN products pro ON sub.product_id = pro.id\n"
    			+ "LEFT JOIN codes cod ON pro.id = cod.product_id\n"
    			+ "LEFT JOIN warehouses war ON inv.warehouse_id = war.id\n"
    			+ "GROUP BY 1, 2, 3, 4, 5, 6\n"
    			+ "ORDER BY 2";
    	
    	List<Object> params = new ArrayList<>();
    	params.add(filterDate + " 23:59:59");
    	if (!Long.valueOf(-1).equals(warehouseId)) {
    	    params.add(warehouseId);
    	}
    	List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, params.toArray());
    	return queryForList;
	}
    
    public List<Warehouse> getWarehouse(Authentication authentication) {
    	return wr.findAll();
	}
    
    public List<Log> getLogs(Authentication authentication) {
    	return lr.findAll(Sort.by("fechahora"));
	}
    
    public List<InventoryMovement> getInventoryMovement(Authentication authentication) {    	
    	return jdbcTemplate.query("SELECT * FROM inventory_movement",
                (rs, rowNum) -> {
                	InventoryMovement tmp = new InventoryMovement();
                	tmp.setId(rs.getLong("id"));
                	//tmp.setProduct(new Product(rs.getInt("product_id")));
                	//tmp.setFromWarehouse(new Warehouse(rs.getInt("warehouse_id")));
                	//tmp.setToWarehouse(new Warehouse(rs.getInt("warehouse_id")));
                	tmp.setQuantity(rs.getInt("quantity"));
                	tmp.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
                	tmp.setFechahora(rs.getObject("fechahora", Instant.class));
                	tmp.setNotes(rs.getString("notes"));
                	//tmp.setUsuario(new Usuario(rs.getInt("usuario_id")));
                    return tmp;
                });
    }
    
    public Long getDbId(List<Product> allProducts, String code) {
    	for (Product product : allProducts) {
    		for (Code dbCode : product.getCode()) {
				if(dbCode.getCode().equals(code)) {
					return product.getId();
				}
			}
		}
    	return null;
    }

    @Transactional
	public Map<String, Integer> uploadFile(Authentication authentication, String fileId, Long warehouseId, MultipartFile mpf) {
		Map<String, Integer> response = new HashMap<>();
		response.put("success", 0);
		response.put("errors", 0);
		Tenant tenant = new Tenant(((UserDto) authentication.getPrincipal()).getTenantId(), null, null);
		Warehouse warehouse = new Warehouse(warehouseId, null, null);
		User user = new User(((UserDto) authentication.getPrincipal()).getId(), null, null, null, null, null, null); 
    	List<Product> allProducts = pr.findAll(Sort.by("id"));    	

		try (Workbook workbook = WorkbookFactory.create(mpf.getInputStream())) {
			Map<String, Object[]> map = new HashMap<>();
			Instant now = Instant.now();
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			DecimalFormat df = new DecimalFormat("#");
		    while (rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        try {
		        	if("ml".equals(fileId)) {
		        		String code = row.getCell(16).getStringCellValue();
		        		String name = row.getCell(17).getStringCellValue();
		        		Long id = getDbId(allProducts, code);
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
		        		String name = row.getCell(29).getStringCellValue();
		        		if (row.getCell(28) != null) {
		        			code = df.format(row.getCell(28).getNumericCellValue());
		        			name = name + " " + row.getCell(30).getStringCellValue();
		        		}
		        		Long id = getDbId(allProducts, code);
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
		    
		    String sqlProducts = "SELECT STRING_AGG(code, ';') AS codes FROM codes WHERE tenant_id = :tenantId AND code IN(:codes)";
		    Query nq = em.createNativeQuery(sqlProducts);
		    nq.setParameter("tenantId", tenant.getId());
		    nq.setParameter("codes", map.keySet());
		    String products = (String) nq.getSingleResult();
		    
		    Set<String> comboCodes = new HashSet<>();
		    for (String excelCode : map.keySet()) {
		    	boolean isInDb = products == null ? false : products.contains(excelCode);		    	
		    	String name = (String) map.get(excelCode)[1];
		    	Integer price = (Integer) map.get(excelCode)[2];
		    	boolean isCombo = name.trim().endsWith("$");
		    	if (isCombo) {
		    	    comboCodes.add(excelCode);
		    	}
		    	if (!isInDb) {
		    	    if (isCombo) {
						Combo combo = new Combo(null, tenant, name, Status.Incompleto, null, null);
						Code code = new Code(null, tenant, excelCode, null, combo);
		    	    	comr.save(combo);
		    	    	codr.save(code);
		    	    } else {
		    	    	Product product = new Product(null, tenant, null, null, name, null, new BigDecimal(price), Status.Incompleto);
		    	    	Code code = new Code(null, tenant, excelCode, product, null);
		    	    	Inventory inventory = new Inventory(null, tenant, warehouse, product, 0, now);
		    	    	pr.save(product);
		    	    	codr.save(code);
		    	    	ir.save(inventory);
		    	    	map.get(excelCode)[3] = product.getId();
		    	    }
		    	}
			}
		    
		    if(!comboCodes.isEmpty()) {
			    String sqlComboToProducts = "SELECT DISTINCT ccd.code AS cmb_code, pro.id AS pro_id, prd.quantity\n"
			    		+ "FROM combos cmb\n"
			    		+ "INNER JOIN codes ccd ON ccd.combo_id = cmb.id\n"
			    		+ "INNER JOIN combos_product_detail cpd ON cmb.id = cpd.combo_id\n"
			    		+ "INNER JOIN product_details prd ON cpd.product_detail_id = prd.id\n"
			    		+ "INNER JOIN products pro ON prd.product_id = pro.id\n"
			    		+ "WHERE cmb.tenant_id = :tenantId\n"
			    		+ "AND ccd.code IN (:comboCodes)\n";
			    Query nqCombos = em.createNativeQuery(sqlComboToProducts);
			    nqCombos.setParameter("tenantId", tenant.getId());
			    nqCombos.setParameter("comboCodes", comboCodes);
				List<Object[]> comboToProducts = nqCombos.getResultList();
				for (int i = 0; i < comboToProducts.size(); i++) {
			    	Object[] tmp = comboToProducts.get(i);
					String cmbCode = (String) tmp[0];
					Long proId = (Long) tmp[1];
					Integer productQuantity = (Integer) tmp[2];
					Integer cmbQuantity = (Integer) map.get(cmbCode)[0];
					map.put("combo " + i, new Object[] {cmbQuantity * productQuantity, "name", "price", proId});
				}
			    for (String code : comboCodes) {
			    	map.remove(code);
				}
		    }
		    
		    String sqlValidation = "SELECT pro.id, inv.quantity\n"
		    		+ "FROM products pro\n"
		    		+ "LEFT JOIN codes cod ON pro.id = cod.product_id\n"
		    		+ "LEFT JOIN inventories inv ON (inv.product_id = pro.id AND inv.warehouse_id = :warehouseId)\n"
		    		+ "WHERE pro.tenant_id = :tenantId\n"
		    		+ "AND inv.quantity IS NULL\n"
		    		+ "AND pro.id IN (:productIds)";		    

		    String sqlSubtraction = "UPDATE inventories inv\n"
		    		+ "SET quantity = inv.quantity - :quantity\n"
		    		+ "FROM products pro\n"
		    		+ "WHERE inv.product_id = pro.id\n"
		    		+ "AND pro.tenant_id = :tenantId\n"
		    		+ "AND pro.id = :productId\n"
		    		+ "AND inv.warehouse_id = :warehouseId";
		    	    
		    List<Long> productIds = map.values().stream().map(val -> ((Number) val[3]).longValue()).toList();
		    Query nqValidation = em.createNativeQuery(sqlValidation);
		    nqValidation.setParameter("warehouseId", warehouse.getId());
		    nqValidation.setParameter("tenantId", tenant.getId());
		    nqValidation.setParameter("productIds", productIds);
		    List<Object[]> resultList = nqValidation.getResultList();
		    for (Object[] tmp : resultList) {
		    	Product product = new Product((Long) tmp[0], null, null, null, null, null, null, null);
		    	Inventory inventory = new Inventory(null, tenant, warehouse, product, 0, now);
		    	ir.save(inventory);
			}		    
		    
		    for (Map.Entry<String, Object[]> entry : map.entrySet()) {
		    	String excelCode = entry.getKey();
		    	Integer excelQuantity = (Integer) entry.getValue()[0];
		    	Long productId = (Long) entry.getValue()[3];
		    	if(!comboCodes.contains(excelCode)) { 
			    	try {
			    		Product product = new Product(productId, null, null, null, null, null, null, null);
			    		InventoryMovement im = new InventoryMovement(null, tenant, product, warehouse, excelQuantity, MovementType.Venta, now, mpf.getOriginalFilename(), user);
			    		imr.save(im);
			    		Query nqSubtraction = em.createNativeQuery(sqlSubtraction);
			    		nqSubtraction.setParameter("quantity", excelQuantity);
			    		nqSubtraction.setParameter("tenantId",tenant.getId());
			    		nqSubtraction.setParameter("productId", productId);
			    		nqSubtraction.setParameter("warehouseId", warehouse.getId());
			    		nqSubtraction.executeUpdate();
			    		response.put("success", response.get("success") + 1);
					} catch (Exception e) {e.printStackTrace();
						Log log = new Log(null, tenant, now, e.getMessage());
						lr.save(log);
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
