package cl.aulaboh.grades.exception;

public class ExternalServiceUnavailableException extends RuntimeException {
    public ExternalServiceUnavailableException(String serviceName) {
        super(serviceName + " no esta disponible temporalmente.");
    }
}
