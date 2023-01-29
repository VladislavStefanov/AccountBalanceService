package bg.stefanov.vladislav.accountbalanceservice.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.stefanov.vladislav.accountbalanceservice.exception.AccountNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.model.Account;
import bg.stefanov.vladislav.accountbalanceservice.model.AccountBalance;
import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;
import bg.stefanov.vladislav.accountbalanceservice.model.Payment;
import bg.stefanov.vladislav.accountbalanceservice.repository.AccountRepository;
import bg.stefanov.vladislav.accountbalanceservice.repository.InvoiceRepository;
import bg.stefanov.vladislav.accountbalanceservice.repository.PaymentRepository;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	public Account getAccount(UUID id) {
		return accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
	}
	
	public Collection<Account> getAccounts() {
		return accountRepository.findAll();
	}

	public Account createAccount(Account account) {
		Account accountWithId = account.toBuilder().id(UUID.randomUUID()).build();
		accountRepository.save(accountWithId);
		return accountWithId;
	}

	public Account updateAccount(UUID id, Account account) {
		Account accountWithId = account;
		if(!id.equals(account.getId())) {
			accountWithId = account.toBuilder().id(id).build();
		}
		
		accountRepository.save(accountWithId);
		
		return accountWithId;
	}

	public void deleteAccount(UUID id) {
		if(!accountRepository.deleteById(id)) {
			throw new AccountNotFoundException();
		}
	}

	public AccountBalance getAccountBalance(UUID accountId, LocalDate date) {
		accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
		
		Collection<Invoice> invoices = invoiceRepository.findAllByAccountIdAndDate(accountId, date);
		Set<UUID> invoiceIds = invoices.stream().map(Invoice::getId).collect(Collectors.toSet());
		
		Collection<Payment> payments = paymentRepository.findAllByInvoiceIdsAndDate(invoiceIds, date);
		
		Double invoiceAmountsSum = invoices.stream().map(Invoice::getAmount).reduce(Double::sum).orElse(0.0);
		Double paymentAmountsSum = payments.stream().map(Payment::getAmount).reduce(Double::sum).orElse(0.0);
		Double balance = invoiceAmountsSum - paymentAmountsSum;
		
		return new AccountBalance(accountId, date, balance);
	}
}
