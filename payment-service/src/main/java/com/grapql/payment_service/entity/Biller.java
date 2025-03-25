package com.grapql.payment_service.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Biller {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String billerName;
	private int billerAccountNumber;

	@OneToMany(mappedBy = "biller", cascade = CascadeType.ALL)
    // One Biller can have multiple Payments.
    // `mappedBy = "biller"` means this is the inverse side of the relationship.
    // CascadeType.ALL ensures that when a Biller is deleted, all related Payments are also deleted.
    private List<Payment> payments = new ArrayList<>();
}
