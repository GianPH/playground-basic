package com.rg.gph.playgroundbasic.mapper;

import com.rg.gph.playgroundbasic.entity.Account;
import com.rg.gph.playgroundbasic.model.AccountDetailQuery;
import com.rg.gph.playgroundbasic.model.AccountGenericQuery;
import com.rg.gph.playgroundbasic.model.CreateAccountCommand;
import com.rg.gph.playgroundbasic.model.UpdateAccountCommand;

import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

	public AccountGenericQuery toAccountGeneric(final Account account) {
		return AccountGenericQuery.builder()
				.id(account.getId())
				.fullName(account.getFullName())
				.emailAddress(account.getEmailAddress())
				.phoneNumber(account.getPhoneNumber())
				.age(account.getAge())
				.build();
	}

	public AccountDetailQuery toAccountDetail(final Account account) {
		return AccountDetailQuery.builder()
				.id(account.getId())
				.fullName(account.getFullName())
				.emailAddress(account.getEmailAddress())
				.phoneNumber(account.getPhoneNumber())
				.age(account.getAge())
				.placeOfBirth(account.getPlaceOfBirth())
				.birthDate(account.getBirthDate())
				.build();
	}

	public Account toAccount(final CreateAccountCommand createAccountCommand) {
		return Account.builder()
				.fullName(createAccountCommand.getFullName())
				.emailAddress(createAccountCommand.getEmailAddress())
				.phoneNumber(createAccountCommand.getPhoneNumber())
				.placeOfBirth(createAccountCommand.getPlaceOfBirth())
				.birthDate(createAccountCommand.getBirthDate())
				.build();
	}

	public Account toAccount(Account account, final UpdateAccountCommand updateAccountCommand) {
		account.setFullName(updateAccountCommand.getFullName());
		account.setEmailAddress(updateAccountCommand.getEmailAddress());
		account.setPhoneNumber(updateAccountCommand.getPhoneNumber());
		account.setPlaceOfBirth(updateAccountCommand.getPlaceOfBirth());
		return account;
	}

}
