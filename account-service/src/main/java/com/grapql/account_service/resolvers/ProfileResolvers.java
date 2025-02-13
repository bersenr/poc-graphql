package com.grapql.account_service.resolvers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.grapql.account_service.entity.Profile;
import com.grapql.account_service.serviceImpl.ProfileServiceImpl;

@Controller
public class ProfileResolvers {

	@Autowired
	private ProfileServiceImpl profileServiceImpl;

	@QueryMapping
	public Profile getProfileById(@Argument Long id) {
		Optional<Profile> profile = profileServiceImpl.getProfile(id);
		return profile.get();
	}

	@QueryMapping
	public List<Profile> getProfiles() {
		return profileServiceImpl.getProfiles();
	}
}
