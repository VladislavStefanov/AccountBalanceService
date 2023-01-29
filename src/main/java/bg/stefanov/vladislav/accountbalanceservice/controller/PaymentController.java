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

import bg.stefanov.vladislav.accountbalanceservice.model.Payment;
import bg.stefanov.vladislav.accountbalanceservice.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/{id}")
	public Payment getPayment(@PathVariable UUID id) {
		return paymentService.getPayment(id);
	}
	
	@GetMapping
	public Collection<Payment> getPayments() {
		return paymentService.getPayments();
	}
	
	@PostMapping
	public Payment createPayment(@RequestBody Payment payment) {
		return paymentService.createPayment(payment);
	}
	
	@PutMapping("/{id}")
	public Payment updatePayment(@PathVariable UUID id, @RequestBody Payment payment) {
		return paymentService.updatePayment(id, payment);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePayment(@PathVariable UUID id) {
		paymentService.deletePayment(id);
		return ResponseEntity.noContent().build();
	}
}
