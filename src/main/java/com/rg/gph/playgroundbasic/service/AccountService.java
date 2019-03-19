package com.rg.gph.playgroundbasic.service;

import java.util.List;
import java.util.Optional;

import com.rg.gph.playgroundbasic.model.AccountDetailQuery;
import com.rg.gph.playgroundbasic.model.AccountGenericQuery;
import com.rg.gph.playgroundbasic.model.CreateAccountCommand;
import com.rg.gph.playgroundbasic.model.DeleteAccountCommand;
import com.rg.gph.playgroundbasic.model.UpdateAccountCommand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

	Optional<AccountDetailQuery> findById(String id);

	boolean existById(String id);

	boolean existByPhoneNumber(String phoneNumber);

	boolean existByEmailAddress(String emailAddress);

	boolean existByPhoneNumber(String phoneNumber, String id);

	boolean existByEmailAddress(String emailAddress, String id);

	List<AccountGenericQuery> findAll();

	Page<AccountGenericQuery> findAll(Pageable pageable);

	AccountGenericQuery create(CreateAccountCommand createAccountCommand);

	AccountGenericQuery update(UpdateAccountCommand updateAccountCommand);

	void delete(DeleteAccountCommand deleteAccountCommand);

}
