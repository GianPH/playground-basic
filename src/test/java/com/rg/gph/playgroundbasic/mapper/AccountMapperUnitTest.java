package com.rg.gph.playgroundbasic.mapper;

import java.time.LocalDate;
import java.util.UUID;

import com.rg.gph.playgroundbasic.entity.Account;
import com.rg.gph.playgroundbasic.model.AccountDetailQuery;
import com.rg.gph.playgroundbasic.model.AccountGenericQuery;
import com.rg.gph.playgroundbasic.model.CreateAccountCommand;
import com.rg.gph.playgroundbasic.model.UpdateAccountCommand;
import org.junit.Assert;
import org.junit.Test;

public class AccountMapperUnitTest {

	private AccountMapper accountMapper = new AccountMapper();

	private Account getFullFieldAccount() {
		return Account.builder()
				.id(UUID.randomUUID().toString())
				.fullName("GianPurwa")
				.phoneNumber("08562201600")
				.emailAddress("gianpurwah@Gmail.com")
				.age(25)
				.placeOfBirth("Bandung")
				.birthDate(LocalDate.of(1995, 05, 02))
				.build();
	}

	@Test
	public void toAccountGeneric_allAccountFieldFilled_shouldReturnSameFieldValue() {
		Account accountFullField = getFullFieldAccount();

		AccountGenericQuery accountGenericQuery = accountMapper.toAccountGeneric(accountFullField);
		Assert.assertEquals(accountFullField.getId(), accountGenericQuery.getId());
		Assert.assertEquals(accountFullField.getFullName(), accountGenericQuery.getFullName());
		Assert.assertEquals(accountFullField.getPhoneNumber(), accountGenericQuery.getPhoneNumber());
		Assert.assertEquals(accountFullField.getEmailAddress(), accountGenericQuery.getEmailAddress());
		Assert.assertEquals(accountFullField.getAge(), accountGenericQuery.getAge());
	}

	@Test
	public void toAccountDetail_allAccountFieldFilled_shouldReturnSameFieldValue() {
		Account accountFullField = getFullFieldAccount();

		AccountDetailQuery accountDetailQuery = accountMapper.toAccountDetail(accountFullField);
		Assert.assertEquals(accountFullField.getId(), accountDetailQuery.getId());
		Assert.assertEquals(accountFullField.getFullName(), accountDetailQuery.getFullName());
		Assert.assertEquals(accountFullField.getPhoneNumber(), accountDetailQuery.getPhoneNumber());
		Assert.assertEquals(accountFullField.getEmailAddress(), accountDetailQuery.getEmailAddress());
		Assert.assertEquals(accountFullField.getAge(), accountDetailQuery.getAge());
		Assert.assertEquals(accountFullField.getBirthDate(), accountDetailQuery.getBirthDate());
		Assert.assertEquals(accountFullField.getPlaceOfBirth(), accountDetailQuery.getPlaceOfBirth());
	}

	@Test
	public void toAccount_allCreateAccountCommandFieldFilled_shouldReturnSameFieldValue() {

		CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
				.fullName("GianPurwa")
				.phoneNumber("08562201600")
				.emailAddress("gianpurwah@Gmail.com")
				.placeOfBirth("Bandung")
				.birthDate(LocalDate.of(1995, 05, 02))
				.build();

		Account account = accountMapper.toAccount(createAccountCommand);
		Assert.assertEquals(createAccountCommand.getFullName(), account.getFullName());
		Assert.assertEquals(createAccountCommand.getPhoneNumber(), account.getPhoneNumber());
		Assert.assertEquals(createAccountCommand.getEmailAddress(), account.getEmailAddress());
		Assert.assertEquals(createAccountCommand.getBirthDate(), account.getBirthDate());
		Assert.assertEquals(createAccountCommand.getPlaceOfBirth(), account.getPlaceOfBirth());
	}

	@Test
	public void toAccount_allUpdateAccountCommandFieldFilled_shouldReturnSameFieldValue() {

		Account oldAccount = Account.builder()
				.id(UUID.randomUUID().toString())
				.fullName("oldTestUser")
				.phoneNumber("08562201600")
				.emailAddress("oldTestAccount@Email.com")
				.placeOfBirth("Cimahi")
				.build();

		UpdateAccountCommand updateAccountCommand = UpdateAccountCommand.builder()
				.id(oldAccount.getId())
				.fullName("newTestUser")
				.phoneNumber("082124279820")
				.emailAddress("newTestUser@Email.com")
				.placeOfBirth("Bandung")
				.build();

		Account newAccount = accountMapper.toAccount(oldAccount, updateAccountCommand);
		Assert.assertEquals(updateAccountCommand.getFullName(), newAccount.getFullName());
		Assert.assertEquals(updateAccountCommand.getPhoneNumber(), newAccount.getPhoneNumber());
		Assert.assertEquals(updateAccountCommand.getEmailAddress(), newAccount.getEmailAddress());
		Assert.assertEquals(oldAccount.getBirthDate(), newAccount.getBirthDate());
		Assert.assertEquals(updateAccountCommand.getPlaceOfBirth(), newAccount.getPlaceOfBirth());
	}

}