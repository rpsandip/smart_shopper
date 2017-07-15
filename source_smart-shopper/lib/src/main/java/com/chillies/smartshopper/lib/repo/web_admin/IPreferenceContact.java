package com.chillies.smartshopper.lib.repo.web_admin;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chillies.smartshopper.lib.model.web_model.PreferenceContact;

public interface IPreferenceContact extends MongoRepository<PreferenceContact, String>{

}
