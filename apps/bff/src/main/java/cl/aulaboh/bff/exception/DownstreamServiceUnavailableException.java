package cl.aulaboh.bff.exception;

public class DownstreamServiceUnavailableException extends RuntimeException {
    public DownstreamServiceUnavailableException(String serviceName) {
        super(serviceName + " no esta disponible temporalmente.");
    }
}
