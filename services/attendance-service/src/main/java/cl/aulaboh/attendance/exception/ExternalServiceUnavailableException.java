package cl.aulaboh.attendance.exception;

public class ExternalServiceUnavailableException extends RuntimeException {
    public ExternalServiceUnavailableException(String serviceName) {
        super(serviceName + " no esta disponible temporalmente.");
    }
}
