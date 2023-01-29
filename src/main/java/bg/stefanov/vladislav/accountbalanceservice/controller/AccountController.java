package bg.stefanov.vladislav.accountbalanceservice.controller;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bg.stefanov.vladislav.accountbalanceservice.model.Account;
import bg.stefanov.vladislav.accountbalanceservice.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/{id}")
	public Account getAccount(@PathVariable UUID id) {
		return accountService.getAccount(id);
	}
	
	@GetMapping
	public Collection<Account> getAccounts() {
		return accountService.getAccounts();
	}
	
	@PostMapping
	public Account createAccount(@RequestBody Account account) {
		return accountService.createAccount(account);
	}
	
	@PutMapping("/{id}")
	public Account updateAccount(@PathVariable UUID id, @RequestBody Account account) {
		return accountService.updateAccount(id, account);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
		accountService.deleteAccount(id);
		return ResponseEntity.noContent().build();
	}
}
