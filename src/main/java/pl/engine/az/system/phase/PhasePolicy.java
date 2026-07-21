package pl.engine.az.system.phase;

public record PhasePolicy(
        long tags,
        ExecutionPropagation propagation
) {
}
