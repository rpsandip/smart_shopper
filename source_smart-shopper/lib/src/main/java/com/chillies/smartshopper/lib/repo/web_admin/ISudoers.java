package com.chillies.smartshopper.lib.repo.web_admin;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chillies.smartshopper.lib.model.web_model.Sudoers;

/**
 * 
 * ISudoers : MongoRepository for @Sudoers Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
public interface ISudoers extends MongoRepository<Sudoers, String> {

	public Sudoers findByUsername(String username);
}
