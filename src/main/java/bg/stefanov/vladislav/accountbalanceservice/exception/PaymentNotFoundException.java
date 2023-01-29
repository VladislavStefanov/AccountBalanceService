package bg.stefanov.vladislav.accountbalanceservice.exception;

public class PaymentNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -5086338901458183006L;

	public PaymentNotFoundException() {
		super("Payment");
	}	
}
