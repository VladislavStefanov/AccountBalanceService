package bg.stefanov.vladislav.accountbalanceservice.repository.inmemory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;
import bg.stefanov.vladislav.accountbalanceservice.repository.InvoiceRepository;

class InMemoryInvoiceRepositoryTest {

	private static final double TEST_AMOUNT = 25.0;

	@Test
	void testFindAllByAccountIdAndDate() {
		InvoiceRepository invoiceRepository = new InMemoryInvoiceRepository();
		UUID accountId = UUID.randomUUID();
		LocalDate date = LocalDate.now();
		
		invoiceRepository.save(Invoice.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).accountId(accountId).issueDate(date).build());
		invoiceRepository.save(Invoice.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).accountId(accountId).issueDate(date).build());
		invoiceRepository.save(Invoice.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).accountId(accountId).issueDate(date).build());
		invoiceRepository.save(Invoice.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).accountId(accountId).issueDate(date.minusDays(5)).build());
		invoiceRepository.save(Invoice.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).accountId(UUID.randomUUID()).issueDate(date).build());
		invoiceRepository.save(Invoice.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).accountId(UUID.randomUUID()).issueDate(date.minusDays(5)).build());
		
		int expectedSize = 3;
		assertEquals(expectedSize, invoiceRepository.findAllByAccountIdAndDate(accountId, date).size());
	}

}
