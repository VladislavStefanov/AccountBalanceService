package bg.stefanov.vladislav.accountbalanceservice.repository.inmemory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import bg.stefanov.vladislav.accountbalanceservice.model.Invoice;
import bg.stefanov.vladislav.accountbalanceservice.repository.InvoiceRepository;

@Repository
public class InMemoryInvoiceRepository implements InvoiceRepository {
	private Map<UUID, Invoice> invoices = new HashMap<UUID, Invoice>();

	@Override
	public Optional<Invoice> findById(UUID id) {
		return Optional.ofNullable(invoices.get(id));
	}
	
	@Override
	public Collection<Invoice> findAllByAccountIdAndDate(UUID accountId, LocalDate date) {
		return invoices.values().stream()
				.filter(invoice -> accountId.equals(invoice.getAccountId()))
				.filter(invoice -> date.equals(invoice.getIssueDate()))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<Invoice> findAll() {
		return invoices.values();
	}

	@Override
	public void save(Invoice invoice) {
		invoices.put(invoice.getId(), invoice);
	}

	@Override
	public boolean deleteById(UUID id) {
		return invoices.remove(id) != null;
	}
}
