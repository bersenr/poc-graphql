package com.grapql.account_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String accountNumber;

	@Column(nullable = false)
	private String accountName;

	private long balance;

	@OneToOne // Defines a one-to-one relationship with the `Profile` entity
	@JoinColumn(name = "account_profile_id", referencedColumnName = "id")
	// Specifies the foreign key column (`account_profile_id`) that links to `Profile.id`
	private Profile accountProfile;

	@Version
	// Enables optimistic locking by maintaining a version number (helps prevent concurrent update conflicts)
	private Integer version;
}
