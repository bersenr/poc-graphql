package com.grapql.account_service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grapql.account_service.entity.Profile;
import com.grapql.account_service.repo.ProfileRepo;
import com.grapql.account_service.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private ProfileRepo profileRepo;

	/**
	 * Creates and saves a new profile.
	 *
	 * @param profile The profile entity to be added.
	 * @return The saved profile.
	 */
	@Override
	public Profile addProfile(Profile profile) {
		return profileRepo.save(profile);
	}

	/**
	 * Retrieves a profile by its unique ID.
	 *
	 * @param id The profile ID.
	 * @return An Optional containing the profile if found, otherwise empty.
	 */
	@Override
	public Optional<Profile> getProfile(Long id) {
		return profileRepo.findById(id);
	}

	/**
	 * Retrieves all profiles from the database.
	 *
	 * @return A list of all profile entities.
	 */
	@Override
	public List<Profile> getProfiles() {
		return profileRepo.findAll();
	}
}
