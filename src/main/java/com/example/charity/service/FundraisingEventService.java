package com.example.charity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.charity.model.CurrencyType;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.repository.FundraisingEventRepository;

@Service
public class FundraisingEventService {
	private final FundraisingEventRepository repo;

	public FundraisingEventService(FundraisingEventRepository repo) {
		this.repo = repo;
	}

	public FundraisingEvent create(String name, CurrencyType currency) {
		FundraisingEvent event = new FundraisingEvent();
		event.setName(name);
		event.setCurrency(currency);
		return repo.save(event);
	}

	public List<FundraisingEvent> getAll() {
		return repo.findAll();
	}
}

