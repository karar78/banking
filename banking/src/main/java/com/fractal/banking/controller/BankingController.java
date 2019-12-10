package com.fractal.banking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.RedirectView;

import com.fractal.banking.model.AuthorizationToken;
import com.fractal.banking.model.Category;
import com.fractal.banking.model.Company;
import com.fractal.banking.model.Transaction;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/fractal")
public class BankingController {
	public static final Logger LOGGER = LoggerFactory.getLogger(BankingController.class);
	private final String baseUri = "https://sandbox.askfractal.com";
	private final String localBaseUri = "http://localhost:8080/fractal";
	
	@Value("${app.header.apiKey}")
	private String apiKey;
	
	@Value("${app.header.contentType}")
	private String contentType;
	
	@Value("${app.header.accept}")
	private String accept;
	
	@Value("${app.header.partnerId}")
	private String partnerId;
	
	@Autowired
	private WebClient webClient;

	@GetMapping("/token")
	public String getToken() {
		String token = webClient.post().uri(baseUri + "/token").headers(headers -> headers.putAll(getHeaders()))
				.retrieve().bodyToFlux(AuthorizationToken.class).blockFirst().getAccess_token();
		return token;
	}

	@PostMapping("/companies")
	@ApiOperation(value = "Create a new company with this URL", hidden = true)
	public Flux<String> createCompany() {
		Company company = new Company(25, "316, Longbridge Road, London", 11, "karar", "www.haider.com", "IT", "GBP");
		return webClient.post().uri("/companies").headers(headers -> headers.putAll(getHeaders(getToken())))
				.body(Mono.just(company), Company.class).retrieve().bodyToFlux(String.class);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/banks")
	@ApiOperation(value="List all banks",
				notes="This is the entry point to navigate to your transactions. Best to be viewed in a browser",
				response = Map.class)
	public String getBanks(Model model) {
		Map<String, String> jsonData = webClient.get().uri(baseUri + "/banking")
				.headers(headers -> headers.putAll(getHeaders(getToken())))
				.retrieve().bodyToFlux(Map.class)
				.blockFirst();
		model.addAttribute("banks", jsonData);
		return "/all-banks";
	}

	private HttpHeaders getHeaders() {
		return getHeaders("");
	}

	private HttpHeaders getHeaders(String token) {	
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Api-Key", this.apiKey);
		headers.add("Content-Type", this.contentType);
		headers.add("Accept", this.accept);
		headers.add("X-Partner-Id", this.partnerId);
		if (token.length() > 0)
			headers.add("Authorization", "Bearer " + token);

		return headers;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/banking")
	public RedirectView createBankAuthURL(@RequestParam long bankId) {
		String redirectURL = localBaseUri + "/banking/" +bankId + "/accounts";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("redirect", redirectURL);
		requestData.put("companyId", "99");//2
		Flux<Map> response = webClient.post().uri("/banking/" + bankId + "/auth")
				.headers(headers -> headers.putAll(getHeaders(getToken())))
				.bodyValue(requestData).retrieve()
				.bodyToFlux(Map.class);
		Map<String, String> result = response.blockFirst();
		return new RedirectView(result.get("auth_url").toString());
	}

	@GetMapping("/consents/{bankId}")
	public Flux<String> consent(@PathVariable String bankId, @RequestParam String code, @RequestParam String id_token,
			@RequestParam String state, HttpSession session) {
		session.setAttribute("code", code);
		session.setAttribute("id_token", id_token);
		session.setAttribute("state", state);
		Flux<String> response = webClient.get().uri("/banking/" + bankId + "/consents")
				.headers(headers -> headers.putAll(getHeaders(getToken())))
				.retrieve().bodyToFlux(String.class);
		return response;
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/banking/{bankId}/accounts")
	public String accounts(@PathVariable String bankId, Model model) {
		Flux<Map> response = webClient.get().uri("/banking/" + bankId + "/accounts")
				.headers(headers -> headers.putAll(getHeaders(getToken())))
				.retrieve().bodyToFlux(Map.class);
		model.addAttribute("accounts", response.blockFirst());
		model.addAttribute("bankId", bankId);
		return "/all-accounts";
	}

	@GetMapping("/banking/{bankId}/accounts/{accountId}/balances")
	@ApiOperation(value="Listing balances as per each account",
	notes="This will display all balances based on an accountId and bankId",
	response = Map.class)
	public Flux<String> balances(@ApiParam(value = "bankId  example: 2", required = true) @PathVariable String bankId, @ApiParam(value = "accountId  example: fakeAcc102", required = true) @PathVariable String accountId) {
		Flux<String> response = webClient.get().uri("/banking/" + bankId + "/accounts/" + accountId + "/balances")
				.headers(headers -> headers.putAll(getHeaders(getToken())))
				.retrieve().bodyToFlux(String.class);
		return response;
	}

	@GetMapping("/banking/{bankId}/accounts/{accountId}/transactions")
	@ApiOperation(value="Listing all transactions",
	notes="This will display a list of all transactions based on an accountId and bankId",
	response = Map.class)
	public String transactions(@ApiParam(value = "bankId  example: 2", required = true) @PathVariable String bankId,  @ApiParam(value = "accountId  example: fakeAcc102", required = true) @PathVariable String accountId, Model model) {
		Flux<Map> response = webClient.get().uri("/banking/" + bankId + "/accounts/" + accountId + "/transactions")
				.headers(headers -> headers.putAll(getHeaders(getToken())))
				.retrieve().bodyToFlux(Map.class);
		model.addAttribute("transactions", response.blockFirst());
		return "/transactions";
	}

	private String getTransactionDetails(String bankId, String accountId) {
		String jsonDetailTransactions = webClient.get()
				.uri("/banking/" + bankId + "/accounts/" + accountId + "/transactions")
				.headers(headers -> headers.putAll(getHeaders(getToken())))
				.retrieve().bodyToFlux(String.class).blockFirst();

		return jsonDetailTransactions;
	}

	@GetMapping("banking/categories/transactions")
	@ApiOperation(value="View transactions along with its category",
	notes="This will display transactionId and description along with its categories for a given companyId. This transactionId somehow is different than transactionId where we view details on transactions",
	response = Map.class)
	public String categorizedTransactions(@ApiParam(value = "CompanyId for a company to retriew transactions example: 2", required = true) @RequestParam String companyId, Model model) {
		String jsonCategorizedTransactions = webClient.get().uri("/categories/transactions?companyId=" + companyId)
				.headers(headers -> headers.putAll(getHeaders(getToken())))
				.retrieve().bodyToFlux(String.class).blockFirst();
		Map<String, Transaction> transactions = parseTransactions(jsonCategorizedTransactions, getCategories());

		List<Transaction> uniqueAccounts = transactions.values().stream()
				.filter(distinctByKey(Transaction::getAccountId)).collect(Collectors.toList());
		for (Transaction transaction : uniqueAccounts) {
			String jsonTransactionDetails = getTransactionDetails(transaction.getBankId(), transaction.getAccountId());
			// parseTransactionDetails(jsonTransactionDetails, transactions);
		}
		model.addAttribute("transactions", transactions);
		return "/detailed-transactions";
	}

	private void parseTransactionDetails(String jsonDetailTransactions, Map<String, Transaction> transactions) {
		JSONObject jsonObject = null;
		// System.err.println(jsonDetailTransactions);
		try {
			jsonObject = new JSONObject(jsonDetailTransactions);
			JSONArray detailedTransactions = jsonObject.getJSONArray("results");

			for (int i = 0; i < detailedTransactions.length(); i++) {
				JSONObject detailedTransaction = detailedTransactions.getJSONObject(i);
				Transaction transaction = transactions.get(detailedTransaction.getString("transactionId"));
				// System.err.println(transaction.toString());
				if (transaction != null) {
					transaction.setDescription(detailedTransaction.getString("description"));
					System.err.println(detailedTransaction.toString());
				}
			}

		} catch (JSONException e) {
		}
	}

	
	private Map<String, Category> getCategories() {
		String jsonCategories = webClient.get().uri("/categories")
				.headers(headers -> headers.putAll(getHeaders(getToken())))
				.retrieve().bodyToFlux(String.class).blockFirst();
		Map<String, Category> categories = getMappedCategories(jsonCategories);
		return categories;
	}

	private Map<String, Category> getMappedCategories(String jsonData) {
		Map<String, Category> categories = new HashMap<String, Category>();
		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(jsonData);
			JSONArray jsonCategories = jsonObject.getJSONArray("results");
			for (int i = 0; i < jsonCategories.length(); i++) {
				JSONObject jsonCategory = jsonCategories.getJSONObject(i);
				Category category = new Category(jsonCategory.getString("id"), jsonCategory.getString("name"));
				categories.put(jsonCategory.getString("name"), category);
			}
		} catch (JSONException e) {
		}

		return categories;
	}

	private Map<String, Transaction> parseTransactions(String jsonCategorizedTransactions,
			Map<String, Category> categories) {
		Map<String, Transaction> transactions = new HashMap<String, Transaction>();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonCategorizedTransactions);
		} catch (JSONException e) {
		}
		JSONArray categorizedTransactions = null;
		try {
			categorizedTransactions = jsonObject.getJSONArray("results");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < categorizedTransactions.length(); i++) {
			JSONObject categorizedTransaction = null;
			try {
				categorizedTransaction = categorizedTransactions.getJSONObject(i);
				Transaction transaction = new Transaction();
				Category category = categories.get(categorizedTransaction.getString("category"));

				transaction.setCompanyId(categorizedTransaction.getString("companyId"))
						.setBankId(categorizedTransaction.getString("bankId"))
						.setAccountId(categorizedTransaction.getString("accountId"))
						.setTransactionId(categorizedTransaction.getString("transactionId"))
						.setBookingDate(categorizedTransaction.getString("bookingDate"))
						.setDescription(categorizedTransaction.getString("description"))
						.setAmount(categorizedTransaction.getDouble("amount")).setCategory(category);
				transactions.put(transaction.getTransactionId(), transaction);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return transactions;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
