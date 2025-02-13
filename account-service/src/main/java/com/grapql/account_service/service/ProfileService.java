package com.grapql.account_service.service;

import java.util.List;
import java.util.Optional;

import com.grapql.account_service.entity.Profile;

public interface ProfileService {
	Profile addProfile(Profile profile);

	Optional<Profile> getProfile(Long id);

	List<Profile> getProfiles();
}
