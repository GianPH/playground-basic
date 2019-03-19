package com.rg.gph.playgroundbasic.resource;

import javax.validation.Valid;
import java.net.URI;

import com.rg.gph.playgroundbasic.model.AccountDetailQuery;
import com.rg.gph.playgroundbasic.model.AccountGenericQuery;
import com.rg.gph.playgroundbasic.model.ApiResponse;
import com.rg.gph.playgroundbasic.model.CreateAccountCommand;
import com.rg.gph.playgroundbasic.model.DeleteAccountCommand;
import com.rg.gph.playgroundbasic.model.UpdateAccountCommand;
import com.rg.gph.playgroundbasic.service.AccountService;
import com.rg.gph.playgroundbasic.validation.validator.CreateAccountValidator;
import com.rg.gph.playgroundbasic.validation.validator.UpdateAccountValidator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountResource {

	private final AccountService accountService;

	private final CreateAccountValidator createAccountValidator;

	private final UpdateAccountValidator updateAccountValidator;

	public AccountResource(AccountService accountService, CreateAccountValidator createAccountValidator, UpdateAccountValidator updateAccountValidator) {
		this.accountService = accountService;
		this.createAccountValidator = createAccountValidator;
		this.updateAccountValidator = updateAccountValidator;
	}

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		if (binder.getTarget() != null) {
			if (CreateAccountCommand.class.equals(binder.getTarget().getClass())) {
				binder.addValidators(createAccountValidator);
			}
			if (UpdateAccountCommand.class.equals(binder.getTarget().getClass())) {
				binder.addValidators(updateAccountValidator);
			}
		}
	}

	@GetMapping(value = "",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<Page<AccountGenericQuery>>> findAllAccounts(@PageableDefault Pageable pageable) {
		return ResponseEntity.ok(ApiResponse.ok()
				.message("Success get accounts")
				.data(accountService.findAll(pageable)));
	}

	@GetMapping(value = "/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<AccountDetailQuery>> findById(@PathVariable String id) {
		return accountService.findById(id)
				.map(accountQuery -> ResponseEntity.ok().body(ApiResponse
						.ok()
						.message(String.format("Success get account with id (%s)", id))
						.data(accountQuery)))
				.orElse(ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse
								.notFound()
								.message(String.format("Account with id (%s) not found", id))
								.build())
				);
	}

	@PostMapping(value = "",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<AccountGenericQuery>> createAccount(
			@RequestBody @Valid CreateAccountCommand createAccountCommand) {
		AccountGenericQuery accountGenericQuery = accountService.create(createAccountCommand);
		return ResponseEntity
				.created(URI.create(String.format("/api/accounts/%s", accountGenericQuery.getId())))
				.body(ApiResponse.ok()
						.message("Success create account")
						.data(accountGenericQuery));
	}

	@PutMapping(value = "",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<AccountGenericQuery>> updateAccount(@RequestBody @Valid UpdateAccountCommand updateAccountCommand) {
		return ResponseEntity.ok(ApiResponse.ok()
				.message("Success update account")
				.data(accountService.update(updateAccountCommand)));
	}

	@DeleteMapping(value = "",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> deleteAccount(@RequestBody @Valid DeleteAccountCommand deleteAccountCommand) {
		accountService.delete(deleteAccountCommand);
		return ResponseEntity.ok(ApiResponse.ok()
				.message("Success delete account")
				.build());
	}

}
