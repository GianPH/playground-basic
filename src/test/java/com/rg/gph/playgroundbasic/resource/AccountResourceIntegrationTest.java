package com.rg.gph.playgroundbasic.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rg.gph.playgroundbasic.entity.Account;
import com.rg.gph.playgroundbasic.model.AccountDetailQuery;
import com.rg.gph.playgroundbasic.model.AccountGenericQuery;
import com.rg.gph.playgroundbasic.model.ApiResponse;
import com.rg.gph.playgroundbasic.model.CreateAccountCommand;
import com.rg.gph.playgroundbasic.model.UpdateAccountCommand;
import com.rg.gph.playgroundbasic.repository.AccountRepository;
import com.rg.gph.playgroundbasic.validation.ErrorCode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountResourceIntegrationTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	public void findAllAccounts_noQueryStringNoData_shouldReturn2xxCodeAndJsonContenType() throws Exception {
		this.mockMvc.perform(get("/api/accounts"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void findById_withRandomId_shouldReturnNotFound() throws Exception {
		this.mockMvc.perform(get(String.format("/api/accounts/%s", UUID.randomUUID().toString())))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void findById_withKnownId_shouldReturn2xxCodeAndMatchFieldValue() throws Exception {

		Account account = Account.builder()
				.fullName("knownUserId")
				.emailAddress("knownUserId@test.com")
				.phoneNumber("082124273980")
				.build();
		accountRepository.save(account);

		String response = this.mockMvc.perform(get(String.format("/api/accounts/%s", account.getId())))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse().getContentAsString();

		ApiResponse<AccountDetailQuery> apiResponse = objectMapper.readValue(response, new TypeReference<ApiResponse<AccountDetailQuery>>() {});
		AccountDetailQuery accountDetailQuery = apiResponse.getData();

		Assert.assertEquals(accountDetailQuery.getId(), account.getId());
		Assert.assertEquals(accountDetailQuery.getFullName(), account.getFullName());
		Assert.assertEquals(accountDetailQuery.getPhoneNumber(), account.getPhoneNumber());
		Assert.assertEquals(accountDetailQuery.getEmailAddress(), account.getEmailAddress());

		accountRepository.delete(account);
	}

	@Test
	public void createAccount_allFieldFlled_shouldReturn201WithExcpectedResponse() throws Exception {
		CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
				.fullName("Create User Test")
				.emailAddress("createUser@test.com")
				.phoneNumber("08544789520")
				.birthDate(LocalDate.of(1990, 05, 02))
				.placeOfBirth("Cimahi")
				.build();

		String stringResponse = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(createAccountCommand)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse().getContentAsString();

		ApiResponse<AccountGenericQuery> apiResponse = objectMapper.readValue(stringResponse, new TypeReference<ApiResponse<AccountGenericQuery>>() {});
		AccountGenericQuery actualResult = apiResponse.getData();

		Assert.assertEquals(createAccountCommand.getFullName(), actualResult.getFullName());
		Assert.assertEquals(createAccountCommand.getPhoneNumber(), actualResult.getPhoneNumber());
		Assert.assertEquals(createAccountCommand.getEmailAddress(), actualResult.getEmailAddress());
		Assert.assertSame(Period.between(createAccountCommand.getBirthDate(), LocalDate.now()).getYears(), actualResult.getAge());

	}

	@Test
	public void createAccount_allNull_shouldReturnBadRequest() throws Exception {
		CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
				.build();

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(createAccountCommand)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));

	}

	@Test
	public void createAccount_emptyFullName_shouldReturnBadRequest() throws Exception {
		CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
				.fullName("")
				.build();

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(createAccountCommand)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));

	}

	@Test
	public void createAccount_emptyEmailAddress_shouldReturnBadRequest() throws Exception {
		CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
				.emailAddress("")
				.build();

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(createAccountCommand)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));

	}

	@Test
	public void createAccount_invalidEmailAddress_shouldReturnBadRequest() throws Exception {
		CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
				.emailAddress("email.com")
				.build();

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(createAccountCommand)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));

	}

	@Test
	public void createAccount_phoneNumberWithCharacter_shouldReturnBadRequest() throws Exception {
		CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
				.phoneNumber("085622016oo")
				.build();

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(createAccountCommand)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));

	}

	@Test
	public void createAccount_duplicatePhoneNumberOrEmail_shouldReturnBadRequest() throws Exception {
		CreateAccountCommand duplicateCase = CreateAccountCommand.builder()
				.fullName("duplicate case")
				.emailAddress("duplicate@test.com")
				.phoneNumber("0856220165897")
				.birthDate(LocalDate.of(1990, 05, 02))
				.placeOfBirth("Cimahi")
				.build();

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(duplicateCase)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));

		String stringResponse = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(duplicateCase)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse().getContentAsString();

		ApiResponse<AccountGenericQuery> apiResponse = objectMapper.readValue(stringResponse, new TypeReference<ApiResponse<AccountGenericQuery>>() {});
		Assert.assertTrue(apiResponse.getErrors().containsKey(ErrorCode.ALREADY_EXIST.getCodeName()));
	}

	@Test
	public void createAccount_nullPlaceOfBirth_shouldReturnBadRequest() throws Exception {
		CreateAccountCommand duplicateCase = CreateAccountCommand.builder()
				.fullName("nullPlaceOfBirthTest")
				.emailAddress("nullPlaceOfBirthTest@test.com")
				.phoneNumber("0784568912")
				.birthDate(LocalDate.of(1990, 05, 02))
				.build();

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(duplicateCase)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void updateAccount_allUpdatetableFieldChanges_shouldReturnOk() throws Exception {
		CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
				.fullName("oldAccount")
				.emailAddress("oldAccount@test.com")
				.phoneNumber("08795462850")
				.birthDate(LocalDate.of(1990, 05, 02))
				.placeOfBirth("Ciamis")
				.build();

		String stringResponse = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(createAccountCommand)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse().getContentAsString();

		ApiResponse<AccountGenericQuery> apiResponse = objectMapper.readValue(stringResponse, new TypeReference<ApiResponse<AccountGenericQuery>>() {});
		String oldAccountId = apiResponse.getData().getId();

		UpdateAccountCommand updateAccountCommand = UpdateAccountCommand.builder()
				.id(oldAccountId)
				.fullName("newFullName")
				.emailAddress("newEmail@test.com")
				.phoneNumber("08564978452")
				.placeOfBirth("Cimahi")
				.build();

		AccountGenericQuery expectedAccount = AccountGenericQuery.builder()
				.id(oldAccountId)
				.fullName("newFullName")
				.emailAddress("newEmail@test.com")
				.phoneNumber("08564978452")
				.age(Period.between(createAccountCommand.getBirthDate(), LocalDate.now()).getYears())
				.build();

		stringResponse = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(updateAccountCommand)))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse().getContentAsString();

		apiResponse = objectMapper.readValue(stringResponse, new TypeReference<ApiResponse<AccountGenericQuery>>() {});
		AccountGenericQuery actualAccount = apiResponse.getData();

		Assert.assertEquals(expectedAccount, actualAccount);

	}

	@Test
	public void updateAccount_withUnknownId_shouldReturnNotFound() throws Exception {

		UpdateAccountCommand updateAccountCommand = UpdateAccountCommand.builder()
				.id(UUID.randomUUID().toString())
				.fullName("unknown")
				.emailAddress("unknown@test.com")
				.phoneNumber("88794569845")
				.placeOfBirth("Cimahi")
				.build();

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(updateAccountCommand)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));

	}
}