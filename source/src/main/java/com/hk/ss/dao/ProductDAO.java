package com.hk.ss.dao;

import org.springframework.data.repository.CrudRepository;

import com.hk.ss.entity.Product;

public interface ProductDAO extends CrudRepository<Product, Long> {

}
