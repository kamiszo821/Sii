package com.example.charity.service;

import com.example.charity.model.CollectionBox;
import com.example.charity.model.CurrencyType;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.repository.CollectionBoxRepository;
import com.example.charity.repository.FundraisingEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionBoxServiceTest {

	@Mock private CollectionBoxRepository boxRepo;
	@Mock private FundraisingEventRepository eventRepo;
	@Mock private CurrencyExchangeService exchangeService;
	@InjectMocks private CollectionBoxService service;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldRegisterBox() {
		CollectionBox box = new CollectionBox();
		when(boxRepo.save(any())).thenReturn(box);

		CollectionBox result = service.registerBox();

		assertNotNull(result);
		verify(boxRepo).save(any());
	}

	@Test
	void shouldThrowWhenAssigningNonEmptyBox() {
		CollectionBox box = new CollectionBox();
		box.setEmpty(false);
		when(boxRepo.findById(1L)).thenReturn(Optional.of(box));

		assertThrows(IllegalStateException.class, () -> service.assign(1L, 1L));
	}

	@Test
	void shouldAssignEmptyBoxToEvent() {
		CollectionBox box = new CollectionBox();
		box.setEmpty(true);
		FundraisingEvent event = new FundraisingEvent();

		when(boxRepo.findById(1L)).thenReturn(Optional.of(box));
		when(eventRepo.findById(2L)).thenReturn(Optional.of(event));

		service.assign(1L, 2L);

		assertTrue(box.isAssigned());
		assertEquals(event, box.getAssignedEvent());
		verify(boxRepo).save(box);
	}

	@Test
	void shouldPutMoneyIntoBox() {
		CollectionBox box = new CollectionBox();
		box.setContents(new HashMap<>());
		when(boxRepo.findById(1L)).thenReturn(Optional.of(box));

		service.putMoney(1L, CurrencyType.USD, new BigDecimal("10.00"));

		assertFalse(box.isEmpty());
		assertEquals(new BigDecimal("10.00"), box.getContents().get(CurrencyType.USD));
	}

	@Test
	void shouldTransferMoneyWithConversion() {
		CollectionBox box = new CollectionBox();
		FundraisingEvent event = new FundraisingEvent();
		event.setCurrency(CurrencyType.EUR);
		event.setTotalAmount(BigDecimal.ZERO);

		box.setContents(new HashMap<>() {{
			put(CurrencyType.USD, new BigDecimal("100"));
		}});
		box.setAssignedEvent(event);

		when(boxRepo.findById(1L)).thenReturn(Optional.of(box));
		when(exchangeService.convert(new BigDecimal("100"), CurrencyType.USD, CurrencyType.EUR))
				.thenReturn(new BigDecimal("90"));

		service.transfer(1L);

		assertEquals(BigDecimal.valueOf(90), event.getTotalAmount());
		assertTrue(box.isEmpty());
		assertTrue(box.getContents().isEmpty());
	}

	@Test
	void shouldDeleteBoxOnUnregister() {
		CollectionBox box = new CollectionBox();
		when(boxRepo.findById(1L)).thenReturn(Optional.of(box));

		service.unregister(1L);

		verify(boxRepo).delete(box);
	}
}
