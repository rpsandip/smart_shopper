package com.hk.ss.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.hk.ss.entity.User;

@Transactional
public interface ProductCategoryDAO extends CrudRepository<User, Long> {

}
