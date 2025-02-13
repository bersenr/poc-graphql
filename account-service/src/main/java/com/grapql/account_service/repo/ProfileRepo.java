package com.grapql.account_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grapql.account_service.entity.Profile;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {

}
