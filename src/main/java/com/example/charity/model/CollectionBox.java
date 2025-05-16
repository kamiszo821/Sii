package com.example.charity.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionBox {

	@Id
	@GeneratedValue
	private Long id;

	private boolean assigned = false;
	private boolean empty = true;

	@ManyToOne
	private FundraisingEvent assignedEvent;

	@ElementCollection
	private Map<CurrencyType, BigDecimal> contents = new HashMap<>();

}
