package com.n26.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.controller.ApplicationController;
import com.n26.initialize.ApplicationInitializer;
import com.n26.service.dto.TransactionRequestDTO;

@SpringBootTest(classes = ApplicationInitializer.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ApplicationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new ApplicationController()).build();
	}

	@Test
	public void checkStatsEndpoint() throws Exception {

		this.mockMvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void incorrectPOSTData() throws Exception {

		Long oldTimestamp = Instant.now().toEpochMilli() - 200000;
		TransactionRequestDTO transactionRequest = new TransactionRequestDTO();
		transactionRequest.setAmount(100);
		transactionRequest.setTimestamp(oldTimestamp);

		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transactionRequest))).andExpect(status().is(204));

	}

	@Test
	public void correctPOSTData() throws Exception {

		Long timestamp = Instant.now().toEpochMilli();
		TransactionRequestDTO transactionRequest = new TransactionRequestDTO();
		transactionRequest.setAmount(100);
		transactionRequest.setTimestamp(timestamp);

		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transactionRequest))).andExpect(status().is(201));

	}

	@Test
	public void postDataAndGetResults() throws Exception {

		Long timestamp = Instant.now().toEpochMilli();

		TransactionRequestDTO transactionRequest1 = new TransactionRequestDTO();
		transactionRequest1.setAmount(100);
		transactionRequest1.setTimestamp(timestamp);

		TransactionRequestDTO transactionRequest2 = new TransactionRequestDTO();
		transactionRequest2.setAmount(300);
		transactionRequest2.setTimestamp(timestamp);

		// Make the two transactions
		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transactionRequest1))).andExpect(status().is(201));

		// Make the two transactions
		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transactionRequest2))).andExpect(status().is(201));

		// check stats
		this.mockMvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.sum").value(400.0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.min").value(100.0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.max").value(300.0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.avg").value(200.0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2));
	}
}
