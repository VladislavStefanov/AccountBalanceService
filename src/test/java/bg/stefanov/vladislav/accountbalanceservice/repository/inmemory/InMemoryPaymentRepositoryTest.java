package bg.stefanov.vladislav.accountbalanceservice.repository.inmemory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import bg.stefanov.vladislav.accountbalanceservice.model.Payment;
import bg.stefanov.vladislav.accountbalanceservice.repository.PaymentRepository;

class InMemoryPaymentRepositoryTest {
	
	private static final double TEST_AMOUNT = 25.0;

	@Test
	void testFindAllByAccountIdAndDate() {
		PaymentRepository paymentRepository = new InMemoryPaymentRepository();
		UUID invoiceId1 = UUID.randomUUID();
		UUID invoiceId2 = UUID.randomUUID();
		LocalDate date = LocalDate.now();
		
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(invoiceId1).date(date).build());
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(invoiceId2).date(date).build());
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(invoiceId2).date(date).build());
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(invoiceId1).date(date.minusDays(5)).build());
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(invoiceId2).date(date.minusDays(5)).build());
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(UUID.randomUUID()).date(date).build());
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(UUID.randomUUID()).date(date.minusDays(5)).build());
		
		int expectedSize = 3;
		assertEquals(expectedSize, paymentRepository.findAllByInvoiceIdsAndDate(Set.of(invoiceId1, invoiceId2), date).size());
	}

	@Test
	void testFindAllByInvoiceId() {
		PaymentRepository paymentRepository = new InMemoryPaymentRepository();
		UUID invoiceId = UUID.randomUUID();
		LocalDate date = LocalDate.now();
		
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(invoiceId).date(date).build());
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(invoiceId).date(date).build());
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(invoiceId).date(date).build());
		paymentRepository.save(Payment.builder().id(UUID.randomUUID()).amount(TEST_AMOUNT).invoiceId(UUID.randomUUID()).date(date).build());
		
		int expectedSize = 3;
		assertEquals(expectedSize, paymentRepository.findAllByInvoiceId(invoiceId).size());
	}
}
