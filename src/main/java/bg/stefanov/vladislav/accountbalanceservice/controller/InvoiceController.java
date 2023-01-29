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

import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;
import bg.stefanov.vladislav.accountbalanceservice.service.InvoiceService;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
	@Autowired
	private InvoiceService invoiceService;
	
	@GetMapping("/{id}")
	public Invoice getInvoice(@PathVariable UUID id) {
		return invoiceService.getInvoice(id);
	}
	
	@GetMapping
	public Collection<Invoice> getInvoices() {
		return invoiceService.getInvoices();
	}
	
	@PostMapping
	public Invoice createInvoice(@RequestBody Invoice invoice) {
		return invoiceService.createInvoice(invoice);
	}
	
	@PutMapping("/{id}")
	public Invoice updateInvoice(@PathVariable UUID id, @RequestBody Invoice invoice) {
		return invoiceService.updateInvoice(id, invoice);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteInvoice(@PathVariable UUID id) {
		invoiceService.deleteInvoice(id);
		return ResponseEntity.noContent().build();
	}
}
