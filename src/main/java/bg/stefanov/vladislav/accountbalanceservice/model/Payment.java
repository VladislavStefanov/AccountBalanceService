package bg.stefanov.vladislav.accountbalanceservice.model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value @Jacksonized @Builder(toBuilder = true)
public class Payment {
	private final UUID id;
	private final UUID invoiceId;
	private final double amount;
	private final LocalDate date;
}
