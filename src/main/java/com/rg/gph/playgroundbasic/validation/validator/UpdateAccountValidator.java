package com.rg.gph.playgroundbasic.validation.validator;

import com.rg.gph.playgroundbasic.model.UpdateAccountCommand;
import com.rg.gph.playgroundbasic.validation.AccountValidationService;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateAccountValidator implements Validator {

	private final AccountValidationService accountValidationService;

	public UpdateAccountValidator(AccountValidationService accountValidationService) {
		this.accountValidationService = accountValidationService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateAccountCommand.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UpdateAccountCommand updateAccountCommand = (UpdateAccountCommand) target;
		accountValidationService.validateId(updateAccountCommand.getId());
		accountValidationService.validateEmailAddress(updateAccountCommand.getId(), updateAccountCommand.getEmailAddress(), errors);
		accountValidationService.validatePhoneNumber(updateAccountCommand.getId(), updateAccountCommand.getPhoneNumber(), errors);
	}

}
