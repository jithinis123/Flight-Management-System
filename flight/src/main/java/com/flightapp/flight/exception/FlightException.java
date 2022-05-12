package com.flightapp.flight.exception;

public class FlightException extends RuntimeException {
    public FlightException() {
    }

    public FlightException(String message) {
        super(message);
    }

    public FlightException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlightException(Throwable cause) {
        super(cause);
    }

    public FlightException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
