package bg.stefanov.vladislav.accountbalanceservice.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = -5008586847585044547L;
	
	private final String resourceType;
	
	@Override
	public String getMessage() {
		return resourceType + " not found.";
	}
}
