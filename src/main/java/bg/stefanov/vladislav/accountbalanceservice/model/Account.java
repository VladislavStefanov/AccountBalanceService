package bg.stefanov.vladislav.accountbalanceservice.model;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value @Jacksonized @Builder(toBuilder = true)
public class Account {
	private final UUID id;
	private final String name;
}
