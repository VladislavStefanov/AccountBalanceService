package bg.stefanov.vladislav.accountbalanceservice.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import bg.stefanov.vladislav.accountbalanceservice.exception.AccountNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.exception.InvoiceNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.model.Account;
import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;
import bg.stefanov.vladislav.accountbalanceservice.repository.AccountRepository;
import bg.stefanov.vladislav.accountbalanceservice.repository.InvoiceRepository;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

	private static final double TEST_AMOUNT = 25.0;
	private static final String TEST_NAME = "testName";
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private InvoiceRepository invoiceRepository;
	
	@InjectMocks
	private InvoiceService invoiceService;
	
	@Test
	void testGetInvoiceSuccess() {
		Invoice expected = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		Mockito.when(invoiceRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
		
		assertEquals(expected, invoiceService.getInvoice(expected.getId()));
	}
	
	@Test
	void testGetInvoiceNotFound() {
		Mockito.when(invoiceRepository.findById(any())).thenReturn(Optional.empty());
		
		assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getInvoice(UUID.randomUUID()));
	}

	@Test
	void testGetInvoices() {
		Invoice expectedInvoice1 = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		Invoice expectedInvoice2 = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		Collection<Invoice> expected = List.of(expectedInvoice1, expectedInvoice2);
		
		Mockito.when(invoiceRepository.findAll()).thenReturn(expected);
		
		assertEquals(expected, invoiceService.getInvoices());
	}

	@Test
	void testCreateInvoiceSuccess() {
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		Mockito.when(accountRepository.findById(invoice.getAccountId())).thenReturn(Optional.of(Account.builder()
				.id(invoice.getId())
				.name(TEST_NAME)
				.build()));
		
		Invoice actual = invoiceService.createInvoice(invoice);
		
		assertEquals(invoice.getAmount(), actual.getAmount(), 0.0001);
		assertEquals(invoice.getIssueDate(), actual.getIssueDate());
		assertEquals(invoice.getAccountId(), actual.getAccountId());
		assertNotEquals(invoice.getId(), actual.getId());
	}
	
	@Test
	void testCreateInvoiceAmountNegative() {
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(-TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(invoice));
	}
	
	@Test
	void testCreateInvoiceMissingAccountId() {
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.build();
		
		assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(invoice));
	}
	
	@Test
	void testCreateInvoiceAccountNotFound() {
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		Mockito.when(accountRepository.findById(invoice.getAccountId())).thenReturn(Optional.empty());
		
		assertThrows(AccountNotFoundException.class, () -> invoiceService.createInvoice(invoice));
	}

	@Test
	void testUpdateInvoiceSuccess() {
		UUID expectedId = UUID.randomUUID();
		
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		Invoice expected = invoice.toBuilder().id(expectedId).build();
		
		Mockito.when(accountRepository.findById(invoice.getAccountId())).thenReturn(Optional.of(Account.builder()
				.id(invoice.getId())
				.name(TEST_NAME)
				.build()));
		
		assertEquals(expected, invoiceService.updateInvoice(expectedId, invoice));
	}
	
	@Test
	void testUpdateInvoiceAmountNegative() {
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(-TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		assertThrows(IllegalArgumentException.class, () -> invoiceService.updateInvoice(UUID.randomUUID(), invoice));
	}
	
	@Test
	void testUpdateInvoiceMissingAccountId() {
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.build();
		
		assertThrows(IllegalArgumentException.class, () -> invoiceService.updateInvoice(UUID.randomUUID(), invoice));
	}
	
	@Test
	void testUpdateInvoiceAccountNotFound() {
		UUID expectedId = UUID.randomUUID();
		
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		Mockito.when(accountRepository.findById(invoice.getAccountId())).thenReturn(Optional.empty());
		
		assertThrows(AccountNotFoundException.class, () -> invoiceService.updateInvoice(expectedId, invoice));
	}
	
	@Test
	void testDeleteInvoiceSuccess() {
		UUID id = UUID.randomUUID();
		Mockito.when(invoiceRepository.deleteById(id)).thenReturn(true);
		
		assertDoesNotThrow(() -> invoiceService.deleteInvoice(id));
	}

	@Test
	void testDeleteInvoiceNotFound() {
		UUID id = UUID.randomUUID();
		Mockito.when(invoiceRepository.deleteById(id)).thenReturn(false);
		
		assertThrows(InvoiceNotFoundException.class, () -> invoiceService.deleteInvoice(id));
	}

}
