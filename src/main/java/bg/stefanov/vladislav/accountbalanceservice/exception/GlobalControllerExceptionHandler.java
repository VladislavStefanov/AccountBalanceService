package bg.stefanov.vladislav.accountbalanceservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
		return handleStatusCode(ex, request, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
		return handleStatusCode(ex, request, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<Object> handleStatusCode(RuntimeException ex, WebRequest request, HttpStatus status) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		ErrorBody errorBody = ErrorBody.builder()
				.status(status.value())
				.title(status.getReasonPhrase())
				.detail(ex.getMessage())
				.instance(servletWebRequest.getRequest().getRequestURI())
				.build();
		return handleExceptionInternal(ex, errorBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}
