package com.example.charity.service;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.charity.model.CurrencyType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyExchangeServiceTest {

	private final CurrencyExchangeService service = new CurrencyExchangeService();

	@Test
	void shouldConvertUsdToEur() {
		BigDecimal result = service.convert(BigDecimal.valueOf(100), CurrencyType.USD, CurrencyType.EUR);
		assertEquals(BigDecimal.valueOf(90.00), result);
	}

	@Test
	void shouldReturnSameAmountIfCurrenciesMatch() {
		BigDecimal result = service.convert(BigDecimal.valueOf(100), CurrencyType.EUR, CurrencyType.EUR);
		assertEquals(BigDecimal.valueOf(100), result);
	}

	@Test
	void shouldFallbackIfConversionNotDefined() {
		BigDecimal result = service.convert(BigDecimal.valueOf(100), CurrencyType.GBP, CurrencyType.USD);
		assertEquals(BigDecimal.valueOf(100), result);
	}

}