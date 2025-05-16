package com.example.charity.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.charity.model.CollectionBox;
import com.example.charity.model.CurrencyType;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.repository.CollectionBoxRepository;
import com.example.charity.repository.FundraisingEventRepository;

@Service
public class CollectionBoxService {

	private final CollectionBoxRepository boxRepo;
	private final FundraisingEventRepository eventRepo;
	private final CurrencyExchangeService exchangeService;

	public CollectionBoxService(CollectionBoxRepository boxRepo,
								FundraisingEventRepository eventRepo,
								CurrencyExchangeService exchangeService) {
		this.boxRepo = boxRepo;
		this.eventRepo = eventRepo;
		this.exchangeService = exchangeService;
	}

	public CollectionBox registerBox() {
		return boxRepo.save(new CollectionBox());
	}

	public List<CollectionBox> listBoxes() {
		return boxRepo.findAll();
	}

	public void assign(Long boxId,
					   Long eventId) {
		CollectionBox box = boxRepo.findById(boxId).orElseThrow();
		if (!box.isEmpty()) {
			throw new IllegalStateException("Box must be empty");
		}
		box.setAssigned(true);
		box.setAssignedEvent(eventRepo.findById(eventId).orElseThrow());
		boxRepo.save(box);
	}

	public void putMoney(Long boxId,
						 CurrencyType currency,
						 BigDecimal amount) {
		CollectionBox box = boxRepo.findById(boxId).orElseThrow();
		box.getContents().merge(currency, amount, BigDecimal::add);
		box.setEmpty(false);
		boxRepo.save(box);
	}

	public void transfer(Long boxId) {
		CollectionBox box = boxRepo.findById(boxId).orElseThrow();
		FundraisingEvent event = box.getAssignedEvent();
		if (event == null) {
			throw new IllegalStateException("Box not assigned to any event");
		}

		for (var entry : box.getContents().entrySet()) {
			BigDecimal converted = exchangeService.convert(entry.getValue(), entry.getKey(), event.getCurrency());
			event.setTotalAmount(event.getTotalAmount().add(converted));
		}

		box.setContents(new HashMap<>());
		box.setEmpty(true);
		boxRepo.save(box);
		eventRepo.save(event);
	}

	public void unregister(Long boxId) {
		CollectionBox box = boxRepo.findById(boxId).orElseThrow();
		boxRepo.delete(box);
	}

}

