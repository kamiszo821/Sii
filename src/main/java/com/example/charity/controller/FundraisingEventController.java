package com.example.charity.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.charity.model.CurrencyType;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.service.FundraisingEventService;

@RestController
@RequestMapping("/events")
public class FundraisingEventController {

	private final FundraisingEventService service;

	public FundraisingEventController(FundraisingEventService service) {
		this.service = service;
	}

	@PostMapping
	public FundraisingEvent create(@RequestBody EventDto dto) {
		return service.create(dto.name(), dto.currency());
	}

	@GetMapping("/report")
	public List<ReportDto> report() {
		return service.getAll().stream()
					  .map(e -> new ReportDto(e.getName(), e.getTotalAmount(), e.getCurrency()))
					  .toList();
	}

	public record EventDto(String name, CurrencyType currency) {

	}

	public record ReportDto(String name, BigDecimal amount, CurrencyType currency) {

	}

}
