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
        return jdbcTemplate.query("SELECT * FROM products",
            (rs, rowNum) -> {
            	Product user = new Product();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSku(rs.getString("sku"));
                return user;
            });
    }

    public void save(Product user) {
        jdbcTemplate.update("INSERT INTO products(name, sku) VALUES (?, ?)",
            user.getName(), user.getSku());
    }
}