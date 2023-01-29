package bg.stefanov.vladislav.accountbalanceservice.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import bg.stefanov.vladislav.accountbalanceservice.model.Account;

public interface AccountRepository {
	Optional<Account> findById(UUID id);
	void save(Account account);
	boolean deleteById(UUID id);
	Collection<Account> findAll();
}
