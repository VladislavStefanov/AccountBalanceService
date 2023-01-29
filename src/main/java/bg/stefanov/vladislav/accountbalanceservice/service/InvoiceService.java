package bg.stefanov.vladislav.accountbalanceservice.service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.stefanov.vladislav.accountbalanceservice.exception.AccountNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.exception.InvoiceNotFoundException;
import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;
import bg.stefanov.vladislav.accountbalanceservice.repository.AccountRepository;
import bg.stefanov.vladislav.accountbalanceservice.repository.InvoiceRepository;

@Service
public class InvoiceService {

	private static final String INVOICE_ACCOUNT_ID_REQUIRED = "Invoice does not have account id.";
	private static final String INVOICE_AMOUNT_POSITIVE = "Invoice amount must be greater than 0.";

	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	public Invoice getInvoice(UUID id) {
		return invoiceRepository.findById(id).orElseThrow(InvoiceNotFoundException::new);
	}
	
	public Collection<Invoice> getInvoices() {
		return invoiceRepository.findAll();
	}

	public Invoice createInvoice(Invoice invoice) {
		if(invoice.getAmount() <= 0) {
			throw new IllegalArgumentException(INVOICE_AMOUNT_POSITIVE);
		}
		
		if(invoice.getAccountId() == null) {
			throw new IllegalArgumentException(INVOICE_ACCOUNT_ID_REQUIRED);
		}
		
		accountRepository.findById(invoice.getAccountId()).orElseThrow(AccountNotFoundException::new);
		
		Invoice invoiceWithId = invoice.toBuilder().id(UUID.randomUUID()).build();
		invoiceRepository.save(invoiceWithId);
		return invoiceWithId;
	}

	public Invoice updateInvoice(UUID id, Invoice invoice) {
		if(invoice.getAmount() <= 0) {
			throw new IllegalArgumentException(INVOICE_AMOUNT_POSITIVE);
		}
		
		if(invoice.getAccountId() == null) {
			throw new IllegalArgumentException(INVOICE_ACCOUNT_ID_REQUIRED);
		}
		
		accountRepository.findById(invoice.getAccountId()).orElseThrow(AccountNotFoundException::new);
		
		Invoice invoiceWithId = invoice;
		if(!id.equals(invoice.getId())) {
			invoiceWithId = invoice.toBuilder().id(id).build();
		}
		
		invoiceRepository.save(invoiceWithId);
		
		return invoiceWithId;
	}

	public void deleteInvoice(UUID id) {
		if(!invoiceRepository.deleteById(id)) {
			throw new InvoiceNotFoundException();
		}
	}
}
