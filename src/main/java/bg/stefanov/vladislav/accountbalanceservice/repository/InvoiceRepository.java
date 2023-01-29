package bg.stefanov.vladislav.accountbalanceservice.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;

public interface InvoiceRepository {
	Optional<Invoice> findById(UUID id);
	void save(Invoice account);
	boolean deleteById(UUID id);
	Collection<Invoice> findAll();
	Collection<Invoice> findAllByAccountIdAndDate(UUID accountId, LocalDate date);
}
