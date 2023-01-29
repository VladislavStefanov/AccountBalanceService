package bg.stefanov.vladislav.accountbalanceservice.model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Value;

@Value
public class AccountBalance {
	private final UUID accountId;
	private final LocalDate date;
	private final Double balance;
}
