package bg.stefanov.vladislav.accountbalanceservice.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import bg.stefanov.vladislav.accountbalanceservice.exception.InvoiceNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.exception.PaymentNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;
import bg.stefanov.vladislav.accountbalanceservice.model.Payment;
import bg.stefanov.vladislav.accountbalanceservice.repository.InvoiceRepository;
import bg.stefanov.vladislav.accountbalanceservice.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
	
	private static final double TEST_AMOUNT = 25.0;
	
	@Mock
	private PaymentRepository paymentRepository;
	
	@Mock
	private InvoiceRepository invoiceRepository;

	@InjectMocks
	private PaymentService paymentService;
	
	@Test
	void testGetPaymentSuccess() {
		Payment expected = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Mockito.when(paymentRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
		
		assertEquals(expected, paymentService.getPayment(expected.getId()));
	}
	
	@Test
	void testGetPaymentNotFound() {
		Payment expected = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Mockito.when(paymentRepository.findById(expected.getId())).thenReturn(Optional.empty());
		
		assertThrows(PaymentNotFoundException.class, () -> paymentService.getPayment(expected.getId()));
	}

	@Test
	void testGetPayments() {
		Payment expectedPayment1 = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Payment expectedPayment2 = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Collection<Payment> expected = List.of(expectedPayment1, expectedPayment2);
		
		Mockito.when(paymentRepository.findAll()).thenReturn(expected);
		
		assertEquals(expected, paymentService.getPayments());
	}

	@Test
	void testCreatePayment() {
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Mockito.when(invoiceRepository.findById(payment.getInvoiceId())).thenReturn(Optional.of(Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build()));
		
		Mockito.when(paymentRepository.findAllByInvoiceId(payment.getInvoiceId())).thenReturn(Collections.emptyList());
		
		Payment actual = paymentService.createPayment(payment);
		
		assertEquals(payment.getAmount(), actual.getAmount(), 0.0001);
		assertEquals(payment.getDate(), actual.getDate());
		assertEquals(payment.getInvoiceId(), actual.getInvoiceId());
		assertNotEquals(payment.getId(), actual.getId());
	}
	
	@Test
	void testCreatePaymentAmountNegative() {
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(-TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(payment));
	}
	
	@Test
	void testCreatePaymentMissingInvoiceId() {
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.build();
		
		assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(payment));
	}
	
	@Test
	void testCreatePaymentInvoiceNotFound() {
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Mockito.when(invoiceRepository.findById(payment.getInvoiceId())).thenReturn(Optional.empty());
		
		assertThrows(InvoiceNotFoundException.class, () -> paymentService.createPayment(payment));
	}
	
	@Test
	void testCreatePaymentAmountsExceedsInvoiceAmount() {
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		Mockito.when(invoiceRepository.findById(payment.getInvoiceId())).thenReturn(Optional.of(invoice));
		
		Mockito.when(paymentRepository.findAllByInvoiceId(payment.getInvoiceId())).thenReturn(List.of(
				Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT / 3)
				.date(LocalDate.now())
				.invoiceId(invoice.getAccountId())
				.build(),
				Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT / 3)
				.date(LocalDate.now())
				.invoiceId(invoice.getAccountId())
				.build()));
		
		assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(payment));
	}

	@Test
	void testUpdatePaymentSuccess() {
		UUID expectedId = UUID.randomUUID();
		
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Mockito.when(invoiceRepository.findById(payment.getInvoiceId())).thenReturn(Optional.of(Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build()));
		
		Mockito.when(paymentRepository.findAllByInvoiceId(payment.getInvoiceId())).thenReturn(Collections.emptyList());
		
		Mockito.when(paymentRepository.findById(expectedId)).thenReturn(Optional.of(payment));
		
		Payment expected = payment.toBuilder().id(expectedId).build();
		
		assertEquals(expected, paymentService.updatePayment(expectedId, payment));
	}
	
	@Test
	void testUpdatePaymentAmountNegative() {
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(-TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		assertThrows(IllegalArgumentException.class, () -> paymentService.updatePayment(UUID.randomUUID(), payment));
	}
	
	@Test
	void testUpdatePaymentMissingInvoiceId() {
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.build();
		
		assertThrows(IllegalArgumentException.class, () -> paymentService.updatePayment(UUID.randomUUID(), payment));
	}
	
	@Test
	void testUpdatePaymentInvoiceNotFoundException() {
		UUID expectedId = UUID.randomUUID();
		
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Mockito.when(invoiceRepository.findById(payment.getInvoiceId())).thenReturn(Optional.empty());
		
		assertThrows(InvoiceNotFoundException.class, () -> paymentService.updatePayment(expectedId, payment));
	}

	@Test
	void testUpdatePaymentAmountsExceedsInvoiceAmount() {
		UUID expectedId = UUID.randomUUID();
		
		Payment payment = Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.date(LocalDate.now())
				.invoiceId(UUID.randomUUID())
				.build();
		
		Invoice invoice = Invoice.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT)
				.issueDate(LocalDate.now())
				.accountId(UUID.randomUUID())
				.build();
		
		Mockito.when(invoiceRepository.findById(payment.getInvoiceId())).thenReturn(Optional.of(invoice));
		
		Mockito.when(paymentRepository.findAllByInvoiceId(payment.getInvoiceId())).thenReturn(List.of(
				Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT / 3)
				.date(LocalDate.now())
				.invoiceId(invoice.getAccountId())
				.build(),
				Payment.builder()
				.id(UUID.randomUUID())
				.amount(TEST_AMOUNT / 3)
				.date(LocalDate.now())
				.invoiceId(invoice.getAccountId())
				.build()));
		
		Mockito.when(paymentRepository.findById(expectedId)).thenReturn(Optional.of(payment.toBuilder().amount(TEST_AMOUNT / 3).build()));
		
		assertThrows(IllegalArgumentException.class, () -> paymentService.updatePayment(expectedId, payment));
	}
	
	@Test
	void testDeletePaymentSuccess() {
		UUID id = UUID.randomUUID();
		Mockito.when(paymentRepository.deleteById(id)).thenReturn(true);
		
		assertDoesNotThrow(() -> paymentService.deletePayment(id));
	}
	
	@Test
	void testDeletePaymentNotFound() {
		UUID id = UUID.randomUUID();
		Mockito.when(paymentRepository.deleteById(id)).thenReturn(false);
		
		assertThrows(PaymentNotFoundException.class, () -> paymentService.deletePayment(id));
	}

}
