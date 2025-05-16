package com.example.charity.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.charity.model.CurrencyType;

@Service
public class CurrencyExchangeService {
	public BigDecimal convert(BigDecimal amount, CurrencyType from, CurrencyType to) {
		if (from == to) return amount;

		if (from == CurrencyType.USD && to == CurrencyType.EUR) return amount.multiply(BigDecimal.valueOf(0.9));
		if (from == CurrencyType.GBP && to == CurrencyType.EUR) return amount.multiply(BigDecimal.valueOf(1.15));
		if (from == CurrencyType.EUR && to == CurrencyType.USD) return amount.multiply(BigDecimal.valueOf(1.1));
		if (from == CurrencyType.EUR && to == CurrencyType.GBP) return amount.multiply(BigDecimal.valueOf(0.85));
		return amount;
	}
}

