package com.almacenaws.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.almacenaws.model.Product;


@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> findAll() {
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

    public void save(Product tmp) {
        jdbcTemplate.update("INSERT INTO product(sku, name, description, category) VALUES (?, ?, ?, ?)",
            tmp.getSku(), tmp.getName(), tmp.getDescription(), tmp.getCategory());
    }
}