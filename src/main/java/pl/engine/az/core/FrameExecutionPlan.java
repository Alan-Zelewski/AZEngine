package pl.engine.az.core;

import lombok.AllArgsConstructor;
import pl.engine.az.system.phase.SystemPhase;

import java.util.Map;

@AllArgsConstructor
public class FrameExecutionPlan {
    private final Map<SystemPhase, Long> activeTags;

    public long getActiveTags(SystemPhase phase) {
        return activeTags.getOrDefault(phase, 0L);
    }
}
