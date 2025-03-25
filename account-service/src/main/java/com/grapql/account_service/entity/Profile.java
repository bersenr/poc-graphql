package com.grapql.account_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fullName;
	private String phoneNumber;

	@OneToOne(mappedBy = "accountProfile")  
    // This is the inverse side of the one-to-one relationship with `Account`
    // `mappedBy = "accountProfile"` refers to the `accountProfile` field in `Account`
    private Account account;
}
