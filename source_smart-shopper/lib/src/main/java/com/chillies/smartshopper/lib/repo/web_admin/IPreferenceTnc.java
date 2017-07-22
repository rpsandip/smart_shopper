package com.chillies.smartshopper.lib.repo.web_admin;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chillies.smartshopper.lib.model.web_model.PreferenceTnc;

public interface IPreferenceTnc extends MongoRepository<PreferenceTnc, String> {

	public PreferenceTnc findAllTopByOrderByIdDesc();
}
