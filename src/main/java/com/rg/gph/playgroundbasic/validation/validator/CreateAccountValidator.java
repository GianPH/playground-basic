package com.rg.gph.playgroundbasic.validation.validator;

import com.rg.gph.playgroundbasic.model.CreateAccountCommand;
import com.rg.gph.playgroundbasic.validation.AccountValidationService;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CreateAccountValidator implements Validator {

	private final AccountValidationService accountValidationService;

	public CreateAccountValidator(AccountValidationService accountValidationService) {
		this.accountValidationService = accountValidationService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return CreateAccountCommand.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CreateAccountCommand createAccountCommand = (CreateAccountCommand) target;
		accountValidationService.validateEmailAddress(createAccountCommand.getEmailAddress(), errors);
		accountValidationService.validatePhoneNumber(createAccountCommand.getPhoneNumber(), errors);
	}

}
