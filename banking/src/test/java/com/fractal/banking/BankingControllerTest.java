package com.fractal.banking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fractal.banking.controller.BankingController;


@RunWith(SpringRunner.class)
@WebMvcTest(BankingController.class)
class BankingControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetBanks() throws Exception {
		mockMvc.perform(get("/fractal/banks"))
		.andExpect(status().isOk())
		.andExpect(view().name("/all-banks"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(content().string(org.hamcrest.Matchers.containsString("HSBC")));
	}
	
	@Test
	void testTransactions() throws Exception {
		String bankId="2";
		String accountId="fakeAcc102";
		mockMvc.perform(get("/fractal/banking/" + bankId + "/accounts/" + accountId + "/transactions"))
		.andExpect(status().isOk())
		.andExpect(view().name("/transactions"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(content().string(org.hamcrest.Matchers.containsString("0ef942ea-d3ad-4f25-857b-4d4bb7f912d8")));
	}
	
	@Test
	void testCategorizedTransactions() throws Exception {
		String companyId = "2";
		mockMvc.perform(get("/fractal/banking/categories/transactions?companyId=" + companyId))
		.andExpect(status().isOk())
		.andExpect(view().name("/detailed-transactions"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(content().string(org.hamcrest.Matchers.containsString("fakeTrx09")));
	}
}
