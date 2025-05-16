package com.example.charity.service;

import com.example.charity.model.CurrencyType;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.repository.FundraisingEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundraisingEventServiceTest {

	@Mock
	private FundraisingEventRepository repo;

	@InjectMocks
	private FundraisingEventService service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldCreateNewEvent() {
		FundraisingEvent event = new FundraisingEvent();
		event.setName("Charity");
		event.setCurrency(CurrencyType.EUR);

		when(repo.save(any())).thenReturn(event);

		FundraisingEvent result = service.create("Charity", CurrencyType.EUR);

		assertEquals("Charity", result.getName());
		assertEquals(CurrencyType.EUR, result.getCurrency());
	}

	@Test
	void shouldReturnAllEvents() {
		FundraisingEvent e = new FundraisingEvent();
		when(repo.findAll()).thenReturn(List.of(e));

		List<FundraisingEvent> all = service.getAll();

		assertEquals(1, all.size());
	}
}
