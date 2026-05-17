package cl.aulaboh.bff.config;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceLoggingAspectTest {
    private final SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
    private final ServiceLoggingAspect aspect = new ServiceLoggingAspect(meterRegistry);
    private final ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
    private final Signature signature = mock(Signature.class);

    @Test
    void returnsProceedResultWhenInvocationSucceeds() throws Throwable {
        prepareSignature();
        when(joinPoint.proceed()).thenReturn("ok");

        Object result = aspect.logExecutionTime(joinPoint);

        assertThat(result).isEqualTo("ok");
        assertThat(meterRegistry.find("aulaboh.method.execution")
                .tags("service", "bff", "class", "SampleFacade", "method", "execute", "outcome", "success")
                .timer()).isNotNull();
    }

    @Test
    void rethrowsWhenInvocationFails() throws Throwable {
        prepareSignature();
        when(joinPoint.proceed()).thenThrow(new IllegalStateException("fallo controlado"));

        assertThatThrownBy(() -> aspect.logExecutionTime(joinPoint))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("fallo controlado");
        assertThat(meterRegistry.find("aulaboh.method.execution")
                .tags("service", "bff", "class", "SampleFacade", "method", "execute", "outcome", "error")
                .timer()).isNotNull();
    }

    private void prepareSignature() {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringType()).thenReturn(SampleFacade.class);
        when(signature.getName()).thenReturn("execute");
    }

    private static class SampleFacade {
    }
}
