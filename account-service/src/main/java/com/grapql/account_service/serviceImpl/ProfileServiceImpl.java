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

	@Override
	public Profile addProfile(Profile profile) {
		return profileRepo.save(profile);
	}

	@Override
	public Optional<Profile> getProfile(Long id) {
		return profileRepo.findById(id);
	}

	@Override
	public List<Profile> getProfiles() {
		return profileRepo.findAll();
	}

}
