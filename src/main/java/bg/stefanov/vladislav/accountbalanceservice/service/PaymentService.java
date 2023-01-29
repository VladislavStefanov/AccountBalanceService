package bg.stefanov.vladislav.accountbalanceservice.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.stefanov.vladislav.accountbalanceservice.exception.InvoiceNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.exception.PaymentNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;
import bg.stefanov.vladislav.accountbalanceservice.model.Payment;
import bg.stefanov.vladislav.accountbalanceservice.repository.InvoiceRepository;
import bg.stefanov.vladislav.accountbalanceservice.repository.PaymentRepository;

@Service
public class PaymentService {
	private static final String PAYMENTS_SURPASS_INVOICE = "Payments amount must not surpass invoice amount.";
	private static final String PAYMENT_INVOICE_ID_REQUIRED = "Payment does not have invoice id.";
	private static final String PAYMENT_AMOUNT_POSITIVE = "Payment amount must be greater than 0.";

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	public Payment getPayment(UUID id) {
		return paymentRepository.findById(id).orElseThrow(PaymentNotFoundException::new);
	}
	
	public Collection<Payment> getPayments() {
		return paymentRepository.findAll();
	}
	
	private Collection<Payment> getPayments(UUID invoiceId) {
		return paymentRepository.findAllByInvoiceId(invoiceId);
	}

	public Payment createPayment(Payment payment) {
		if(payment.getAmount() <= 0) {
			throw new IllegalArgumentException(PAYMENT_AMOUNT_POSITIVE);
		}
		
		if(payment.getInvoiceId() == null) {
			throw new IllegalArgumentException(PAYMENT_INVOICE_ID_REQUIRED);
		}
		
		Invoice invoice = invoiceRepository.findById(payment.getInvoiceId()).orElseThrow(InvoiceNotFoundException::new);
		
		Double currentAmount = getPayments(payment.getInvoiceId()).stream()
				.map(Payment::getAmount)
				.reduce(Double::sum)
				.orElse(0.0);
		
		if (currentAmount + payment.getAmount() > invoice.getAmount()) {
			throw new IllegalArgumentException(PAYMENTS_SURPASS_INVOICE);
		}
		
		Payment paymentWithId = payment.toBuilder().id(UUID.randomUUID()).build();
		paymentRepository.save(paymentWithId);
		return paymentWithId;
	}

	public Payment updatePayment(UUID id, Payment payment) {
		if(payment.getAmount() <= 0) {
			throw new IllegalArgumentException(PAYMENT_AMOUNT_POSITIVE);
		}
		
		if(payment.getInvoiceId() == null) {
			throw new IllegalArgumentException(PAYMENT_INVOICE_ID_REQUIRED);
		}
		
		Invoice invoice = invoiceRepository.findById(payment.getInvoiceId()).orElseThrow(InvoiceNotFoundException::new);
		
		Optional<Payment> oldPayment = paymentRepository.findById(id);
		double oldPaymentAmount = 0;
		if(oldPayment.isPresent()) {
			oldPaymentAmount = oldPayment.get().getAmount();
		}
		
		Double currentAmount = getPayments(payment.getInvoiceId()).stream()
				.map(Payment::getAmount)
				.reduce(Double::sum)
				.orElse(0.0);
		
		if (currentAmount + payment.getAmount() - oldPaymentAmount > invoice.getAmount()) {
			throw new IllegalArgumentException(PAYMENTS_SURPASS_INVOICE);
		}
		
		Payment paymentWithId = payment;
		if(!id.equals(payment.getId())) {
			paymentWithId = payment.toBuilder().id(id).build();
		}
		
		paymentRepository.save(paymentWithId);
		
		return paymentWithId;
	}

	public void deletePayment(UUID id) {
		if(!paymentRepository.deleteById(id)) {
			throw new PaymentNotFoundException();
		}
	}
}
