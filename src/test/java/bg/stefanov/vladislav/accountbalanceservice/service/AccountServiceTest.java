package bg.stefanov.vladislav.accountbalanceservice.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import bg.stefanov.vladislav.accountbalanceservice.exception.AccountNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.model.Account;
import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;
import bg.stefanov.vladislav.accountbalanceservice.model.Payment;
import bg.stefanov.vladislav.accountbalanceservice.repository.AccountRepository;
import bg.stefanov.vladislav.accountbalanceservice.repository.InvoiceRepository;
import bg.stefanov.vladislav.accountbalanceservice.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	private static final String TEST_NAME = "testName";

	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private InvoiceRepository invoiceRepository;
	
	@Mock
	private PaymentRepository paymentRepository;
	
	@InjectMocks
	private AccountService accountService;
	
	@Test
	void testGetAccountSuccess() {
		UUID id = UUID.randomUUID();
		Account expected = Account.builder().id(id).name(TEST_NAME).build();
		Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(expected));
		
		assertEquals(expected, accountService.getAccount(id));
	}
	
	@Test
	void testGetAccountNotFound() {
		UUID id = UUID.randomUUID();
		Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());
		
		assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(id));
	}
	
	@Test
	void testGetAccounts() {
		Account expectedAccount1 = Account.builder().id(UUID.randomUUID()).name(TEST_NAME).build();
		Account expectedAccount2 = Account.builder().id(UUID.randomUUID()).name(TEST_NAME).build();
		Collection<Account> expected = List.of(expectedAccount1, expectedAccount2);
		
		Mockito.when(accountRepository.findAll()).thenReturn(expected);
		
		assertEquals(expected, accountService.getAccounts());
	}
	
	@Test
	void testCreateAccount() {
		Account account = Account.builder().id(UUID.randomUUID()).name(TEST_NAME).build();
		Account actual = accountService.createAccount(account);
		
		assertEquals(account.getName(), actual.getName());
		assertNotEquals(account.getId(), actual.getId());
	}
	
	@Test
	void testUpdateAccount() {
		Account account = Account.builder().id(UUID.randomUUID()).name(TEST_NAME).build();
		UUID expectedId = UUID.randomUUID();
		Account expected = account.toBuilder().id(expectedId).build();
		
		assertEquals(expected, accountService.updateAccount(expectedId, account));
	}
	
	@Test
	void testDeleteAccountSuccess() {
		UUID id = UUID.randomUUID();
		Mockito.when(accountRepository.deleteById(id)).thenReturn(true);
		
		assertDoesNotThrow(() -> accountService.deleteAccount(id));
	}
	
	@Test
	void testDeleteAccountNotFound() {
		UUID id = UUID.randomUUID();
		Mockito.when(accountRepository.deleteById(id)).thenReturn(false);
		
		assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(id));
	}
	
	@Test
	void testGetAccountBalanceSuccess() {
		UUID accountId = UUID.randomUUID();
		Account account = Account.builder().name(TEST_NAME).id(accountId).build();
		Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
		
		LocalDate date = LocalDate.now();
		
		Set<UUID> invoiceIds = Set.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
		
		Collection<Invoice> invoices = invoiceIds.stream()
				.map(invoiceId -> Invoice.builder().id(invoiceId).amount(25.0).issueDate(date).accountId(accountId).build())
				.collect(Collectors.toList());
		
		Collection<Payment> payments = invoiceIds.stream()
				.map(invoiceId -> Payment.builder().id(UUID.randomUUID()).amount(10.0).date(date).invoiceId(invoiceId).build())
				.collect(Collectors.toList());
		
		Mockito.when(invoiceRepository.findAllByAccountIdAndDate(accountId, date)).thenReturn(invoices);
		Mockito.when(paymentRepository.findAllByInvoiceIdsAndDate(invoiceIds, date)).thenReturn(payments);
		
		assertEquals(45.0, accountService.getAccountBalance(accountId, date).getBalance(), 0.0001);
	}
	
	@Test
	void testGetAccountBalanceNotFound() {
		UUID id = UUID.randomUUID();
		Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());
		
		assertThrows(AccountNotFoundException.class, () -> accountService.getAccountBalance(id, LocalDate.now()));
	}

}
