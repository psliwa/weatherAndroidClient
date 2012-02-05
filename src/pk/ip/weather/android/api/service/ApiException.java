package pk.ip.weather.android.api.service;

public class ApiException extends RuntimeException {
	public ApiException(String message, Throwable t) {
		super(message, t);
	}
	
	public ApiException(String message) {
		super(message);
	}
}
