package ch.sircremefresh.transport;

public class TransportApiException extends RuntimeException {
	public TransportApiException() {
	}

	public TransportApiException(String message) {
		super(message);
	}

	public TransportApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransportApiException(Throwable cause) {
		super(cause);
	}

	public TransportApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
