package cl.aulaboh.grades.exception;

public class DuplicateEvaluationException extends RuntimeException {
    public DuplicateEvaluationException() {
        super("Ya existe una evaluacion con el mismo curso, asignatura, titulo y fecha");
    }
}
