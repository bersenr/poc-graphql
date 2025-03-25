package com.grapql.payment_service.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String accountNumber;
	private long amount;
	private LocalDate paymentDate;

	@ManyToOne
    @JoinColumn(name = "biller_id", nullable = false)
    // Many Payments belong to one Biller.
    // `biller_id` is the foreign key column in the Payment table.
    // `nullable = false` ensures that every Payment must be linked to a Biller.
    private Biller biller;
}
