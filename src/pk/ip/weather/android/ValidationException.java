package pk.ip.weather.android;

public class ValidationException extends RuntimeException {
	public ValidationException(String message, Throwable t) {
		super(message, t);
	}
	
	public ValidationException(String message) {
		super(message);
	}
}
