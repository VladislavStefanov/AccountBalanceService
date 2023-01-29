package bg.stefanov.vladislav.accountbalanceservice.exception;

public class AccountNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 5441533253958547485L;

	public AccountNotFoundException() {
		super("Account");
	}

}
