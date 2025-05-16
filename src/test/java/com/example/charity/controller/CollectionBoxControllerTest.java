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

import com.example.charity.model.CollectionBox;
import com.example.charity.model.CurrencyType;
import com.example.charity.service.CollectionBoxService;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CollectionBoxController.class)
class CollectionBoxControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CollectionBoxService service;

	@TestConfiguration
	static class TestConfig {

		@Bean
		public CollectionBoxService mockCollectionBoxService() {
			return Mockito.mock(CollectionBoxService.class);
		}

	}

	@Test
	void shouldRegisterBox() throws Exception {
		Mockito.when(service.registerBox()).thenReturn(new CollectionBox());

		mockMvc.perform(post("/boxes"))
			   .andExpect(status().isOk());
	}

	@Test
	void shouldAssignBox() throws Exception {
		doNothing().when(service).assign(1L, 2L);

		mockMvc.perform(put("/boxes/1/assign/2"))
			   .andExpect(status().isOk());
	}

	@Test
	void shouldPutMoney() throws Exception {
		String json = """
				    {
				      "currency": "EUR",
				      "amount": 123.45
				    }
				""";

		doNothing().when(service).putMoney(1L, CurrencyType.EUR, new BigDecimal("123.45"));

		mockMvc.perform(post("/boxes/1/put-money")
								.contentType(MediaType.APPLICATION_JSON)
								.content(json))
			   .andExpect(status().isOk());
	}

	@Test
	void shouldTransferMoney() throws Exception {
		doNothing().when(service).transfer(1L);

		mockMvc.perform(post("/boxes/1/transfer"))
			   .andExpect(status().isOk());
	}

	@Test
	void shouldUnregisterBox() throws Exception {
		doNothing().when(service).unregister(1L);

		mockMvc.perform(delete("/boxes/1"))
			   .andExpect(status().isOk());
	}

	@Test
	void shouldListBoxes() throws Exception {
		CollectionBox box = new CollectionBox();
		box.setId(1L);
		box.setAssigned(false);
		box.setEmpty(true);

		Mockito.when(service.listBoxes()).thenReturn(List.of(box));

		mockMvc.perform(get("/boxes"))
			   .andExpect(status().isOk());
	}

}
