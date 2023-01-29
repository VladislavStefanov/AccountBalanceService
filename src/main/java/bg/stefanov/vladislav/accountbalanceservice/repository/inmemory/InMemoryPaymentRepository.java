package bg.stefanov.vladislav.accountbalanceservice.repository.inmemory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import bg.stefanov.vladislav.accountbalanceservice.model.Payment;
import bg.stefanov.vladislav.accountbalanceservice.repository.PaymentRepository;

@Repository
public class InMemoryPaymentRepository implements PaymentRepository{
	private Map<UUID, Payment> payments = new HashMap<UUID, Payment>();

	@Override
	public Optional<Payment> findById(UUID id) {
		return Optional.ofNullable(payments.get(id));
	}
	
	@Override
	public Collection<Payment> findAll() {
		return payments.values();
	}
	
	@Override
	public Collection<Payment> findAllByInvoiceId(UUID invoiceId) {
		return payments.values().stream().filter(payment -> invoiceId.equals(payment.getInvoiceId())).collect(Collectors.toList());
	}
	
	@Override
	public Collection<Payment> findAllByInvoiceIdsAndDate(Set<UUID> invoiceIds, LocalDate date) {
		return payments.values().stream()
				.filter(payment -> invoiceIds.contains(payment.getInvoiceId()))
				.filter(payment -> date.equals(payment.getDate()))
				.collect(Collectors.toList());
	}

	@Override
	public void save(Payment payment) {
		payments.put(payment.getId(), payment);
	}

	@Override
	public boolean deleteById(UUID id) {
		return payments.remove(id) != null;
	}
}
