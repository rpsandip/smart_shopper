package com.chillies.smartshopper.lib.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chillies.smartshopper.lib.model.Users;

/**
 * 
 * ISudoers : MongoRepository for @Sudoers Document or Collection.
 * 
 * @author Jinen Kothari
 *
 */
public interface IUsers extends MongoRepository<Users, String> {

	public Users findByUsername(String username);

	public Users findByContactMetaPhoneNo(String phoneNo);

	public Users findByReferralCode(String referralCode);
}
