package bg.stefanov.vladislav.accountbalanceservice.repository.inmemory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import bg.stefanov.vladislav.accountbalanceservice.model.Account;
import bg.stefanov.vladislav.accountbalanceservice.repository.AccountRepository;

@Repository
public class InMemoryAccountRepository implements AccountRepository {
	private Map<UUID, Account> accounts = new HashMap<UUID, Account>();

	@Override
	public Optional<Account> findById(UUID id) {
		return Optional.ofNullable(accounts.get(id));
	}

	@Override
	public Collection<Account> findAll() {
		return accounts.values();
	}
	
	@Override
	public void save(Account account) {
		accounts.put(account.getId(), account);
	}

	@Override
	public boolean deleteById(UUID id) {
		return accounts.remove(id) != null;
	}
}
