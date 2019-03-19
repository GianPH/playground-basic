package com.rg.gph.playgroundbasic.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rg.gph.playgroundbasic.entity.Account;
import com.rg.gph.playgroundbasic.exception.ResourceNotFoundException;
import com.rg.gph.playgroundbasic.mapper.AccountMapper;
import com.rg.gph.playgroundbasic.model.AccountDetailQuery;
import com.rg.gph.playgroundbasic.model.AccountGenericQuery;
import com.rg.gph.playgroundbasic.model.CreateAccountCommand;
import com.rg.gph.playgroundbasic.model.DeleteAccountCommand;
import com.rg.gph.playgroundbasic.model.UpdateAccountCommand;
import com.rg.gph.playgroundbasic.repository.AccountRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class DefaultAccountService implements AccountService {

	private final AccountRepository accountRepository;

	private final AccountMapper accountMapper;

	public DefaultAccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
		this.accountRepository = accountRepository;
		this.accountMapper = accountMapper;
	}

	@Override
	public Optional<AccountDetailQuery> findById(String id) {
		return accountRepository.findById(id).map(accountMapper::toAccountDetail);
	}

	@Override
	public boolean existById(String id) {
		return accountRepository.existsById(id);
	}

	@Override
	public boolean existByPhoneNumber(String phoneNumber) {
		return accountRepository.existsAccountByPhoneNumber(phoneNumber);
	}

	@Override
	public boolean existByEmailAddress(String emailAddress) {
		return accountRepository.existsAccountByEmailAddress(emailAddress);
	}

	@Override
	public boolean existByPhoneNumber(String phoneNumber, String id) {
		return accountRepository.existsAccountByPhoneNumberAndIdIsNot(phoneNumber, id);
	}

	@Override
	public boolean existByEmailAddress(String emailAddress, String id) {
		return accountRepository.existsAccountByEmailAddressAndIdIsNot(emailAddress, id);
	}

	@Override
	public List<AccountGenericQuery> findAll() {
		return accountRepository.findAll()
				.stream()
				.map(accountMapper::toAccountGeneric)
				.collect(Collectors.toList());
	}

	@Override
	public Page<AccountGenericQuery> findAll(Pageable pageable) {
		return accountRepository.findAll(pageable)
				.map(accountMapper::toAccountGeneric);
	}

	@Transactional
	@Override
	public AccountGenericQuery create(CreateAccountCommand createAccountCommand) {
		Account account = accountMapper.toAccount(createAccountCommand);
		resolveAge(account);
		accountRepository.save(account);
		return accountMapper.toAccountGeneric(account);
	}

	@Transactional
	@Override
	public AccountGenericQuery update(UpdateAccountCommand updateAccountCommand) {
		Account account = accountRepository
				.findById(updateAccountCommand.getId())
				.orElseThrow(() -> new ResourceNotFoundException(updateAccountCommand.getId(), "Account"));
		accountRepository.save(accountMapper.toAccount(account, updateAccountCommand));
		return accountMapper.toAccountGeneric(account);
	}

	@Transactional
	@Override
	public void delete(DeleteAccountCommand deleteAccountCommand) {
		Account account = accountRepository.findById(deleteAccountCommand.getId())
				.orElseThrow(() -> new ResourceNotFoundException(deleteAccountCommand.getId(), "Account"));
		accountRepository.delete(account);
	}

	private void resolveAge(Account account) {
		account.setAge(Period.between(account.getBirthDate(), LocalDate.now()).getYears());
	}

}
