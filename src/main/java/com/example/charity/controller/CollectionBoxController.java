package com.example.charity.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.charity.dtos.BoxDto;
import com.example.charity.model.CollectionBox;
import com.example.charity.model.CurrencyType;
import com.example.charity.service.CollectionBoxService;

@RestController
@RequestMapping("/boxes")
public class CollectionBoxController {

	private final CollectionBoxService service;

	public CollectionBoxController(CollectionBoxService service) {
		this.service = service;
	}

	@PostMapping
	public CollectionBox register() {
		return service.registerBox();
	}

	@GetMapping
	public List<BoxDto> list() {
		return service.listBoxes().stream()
					  .map(box -> new BoxDto(box.getId(), box.isAssigned(), box.isEmpty()))
					  .toList();
	}


	@PutMapping("/{id}/assign/{eventId}")
	public void assign(@PathVariable Long id,
					   @PathVariable Long eventId) {
		service.assign(id, eventId);
	}

	@PostMapping("/{id}/put-money")
	public void put(@PathVariable Long id,
					@RequestBody PutMoneyDto dto) {
		service.putMoney(id, dto.currency(), dto.amount());
	}

	@PostMapping("/{id}/transfer")
	public void transfer(@PathVariable Long id) {
		service.transfer(id);
	}

	@DeleteMapping("/{id}")
	public void unregister(@PathVariable Long id) {
		service.unregister(id);
	}

	public record PutMoneyDto(CurrencyType currency, BigDecimal amount) {

	}

}
