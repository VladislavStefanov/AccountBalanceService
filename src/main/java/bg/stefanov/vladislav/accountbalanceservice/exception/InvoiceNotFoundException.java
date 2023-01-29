package bg.stefanov.vladislav.accountbalanceservice.exception;

public class InvoiceNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 4283763182362943096L;
	
	public InvoiceNotFoundException() {
		super("Invoice");
	}
}
