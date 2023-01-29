package bg.stefanov.vladislav.accountbalanceservice.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bg.stefanov.vladislav.accountbalanceservice.model.AccountBalance;
import bg.stefanov.vladislav.accountbalanceservice.service.AccountService;

@RestController
@RequestMapping("/balances")
public class AccountBalanceController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping
	public AccountBalance getAccountBalance(@RequestParam("accountId") UUID accountId, @RequestParam("date") LocalDate date) {
		return accountService.getAccountBalance(accountId, date);
	}
}
