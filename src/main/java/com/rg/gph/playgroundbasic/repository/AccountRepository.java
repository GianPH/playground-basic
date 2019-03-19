package com.rg.gph.playgroundbasic.repository;

import com.rg.gph.playgroundbasic.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

	boolean existsAccountByEmailAddress(String emailAddress);

	boolean existsAccountByPhoneNumber(String phoneNumber);

	boolean existsAccountByPhoneNumberAndIdIsNot(String phoneNumber, String id);

	boolean existsAccountByEmailAddressAndIdIsNot(String emailAddress, String id);
}
