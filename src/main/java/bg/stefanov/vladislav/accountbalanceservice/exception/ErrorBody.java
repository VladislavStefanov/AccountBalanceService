package bg.stefanov.vladislav.accountbalanceservice.exception;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value @Jacksonized @Builder
public class ErrorBody {
	private final int status;
	private final String title;
	private final String detail;
	private final String instance;
}
