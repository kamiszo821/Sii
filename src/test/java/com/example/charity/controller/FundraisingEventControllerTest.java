package com.example.charity.controller;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.charity.model.CurrencyType;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.service.FundraisingEventService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FundraisingEventController.class)
class FundraisingEventControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FundraisingEventService service;

	@TestConfiguration
	static class TestConfig {

		@Bean
		public FundraisingEventService mockEventService() {
			return Mockito.mock(FundraisingEventService.class);
		}

	}

	@Test
	void shouldCreateFundraisingEvent() throws Exception {
		FundraisingEvent event = new FundraisingEvent();
		event.setName("Pomoc");
		event.setCurrency(CurrencyType.EUR);
		event.setTotalAmount(BigDecimal.ZERO);

		Mockito.when(service.create("Pomoc", CurrencyType.EUR)).thenReturn(event);

		String json = """
				    {
				      "name": "Pomoc",
				      "currency": "EUR"
				    }
				""";

		mockMvc.perform(post("/events")
								.contentType(MediaType.APPLICATION_JSON)
								.content(json))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.name").value("Pomoc"))
			   .andExpect(jsonPath("$.currency").value("EUR"));
	}

	@Test
	void shouldReturnFinancialReport() throws Exception {
		FundraisingEvent event = new FundraisingEvent();
		event.setName("Charity One");
		event.setCurrency(CurrencyType.EUR);
		event.setTotalAmount(new BigDecimal("2048.00"));

		Mockito.when(service.getAll()).thenReturn(List.of(event));

		mockMvc.perform(get("/events/report"))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$[0].name").value("Charity One"))
			   .andExpect(jsonPath("$[0].amount").value(2048.00))
			   .andExpect(jsonPath("$[0].currency").value("EUR"));
	}

}
