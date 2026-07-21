package pl.engine.az.core;

import pl.engine.az.system.phase.SystemPhase;

import java.util.Map;

public record FrameExecutionPlan(
        Map<SystemPhase, Long> masks
) {

    public long getMask(SystemPhase phase) {
        return masks.getOrDefault(phase, 0L);
    }
}
