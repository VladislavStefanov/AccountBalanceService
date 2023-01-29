package bg.stefanov.vladislav.accountbalanceservice.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import bg.stefanov.vladislav.accountbalanceservice.model.Payment;

public interface PaymentRepository {
	Optional<Payment> findById(UUID id);
	void save(Payment account);
	boolean deleteById(UUID id);
	Collection<Payment> findAll();
	Collection<Payment> findAllByInvoiceId(UUID invoiceId);
	Collection<Payment> findAllByInvoiceIdsAndDate(Set<UUID> invoiceIds, LocalDate date);
}
