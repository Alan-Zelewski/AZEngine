package pl.engine.az.scene;

import pl.engine.az.system.phase.PhasePolicy;
import pl.engine.az.system.phase.SystemPhase;

import java.util.Map;

public record SceneExecutionPolicy(Map<SystemPhase, PhasePolicy> phases) {
    public SceneExecutionPolicy(Map<SystemPhase, PhasePolicy> phases) {
        this.phases = Map.copyOf(phases);
    }
}
