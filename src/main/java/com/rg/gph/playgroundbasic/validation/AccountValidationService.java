package com.rg.gph.playgroundbasic.validation;

import com.rg.gph.playgroundbasic.exception.ResourceNotFoundException;
import com.rg.gph.playgroundbasic.service.AccountService;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class AccountValidationService {

	private final AccountService accountService;

	public AccountValidationService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void validateId(String id) {
		if (!accountService.existById(id)) {
			throw new ResourceNotFoundException(id, "Account");
		}
	}

	public void validateEmailAddress(String id, String emailAddress, Errors errors) {
		if (accountService.existByEmailAddress(emailAddress, id)) {
			errors.rejectValue("emailAddress", ErrorCode.ALREADY_EXIST.getCodeName(), "emailAddress already exist");
		}
	}

	public void validateEmailAddress(String emailAddress, Errors errors) {
		if (accountService.existByEmailAddress(emailAddress)) {
			errors.rejectValue("emailAddress", ErrorCode.ALREADY_EXIST.getCodeName(), "emailAddress already exist");
		}
	}

	public void validatePhoneNumber(String id, String phoneNumber, Errors errors) {
		if (accountService.existByPhoneNumber(phoneNumber, id)) {
			errors.rejectValue("phoneNumber", ErrorCode.ALREADY_EXIST.getCodeName(), "phoneNumber already exist");
		}
	}

	public void validatePhoneNumber(String phoneNumber, Errors errors) {
		if (accountService.existByPhoneNumber(phoneNumber)) {
			errors.rejectValue("phoneNumber", ErrorCode.ALREADY_EXIST.getCodeName(), "phoneNumber already exist");
		}
	}

}
