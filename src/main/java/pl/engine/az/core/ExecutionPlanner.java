package pl.engine.az.core;

import pl.engine.az.scene.Scene;
import pl.engine.az.scene.SceneExecutionPolicy;
import pl.engine.az.system.phase.*;

import java.util.*;

public final class ExecutionPlanner {
    private final List<SystemPhase> phases;

    public ExecutionPlanner() {
        List<SystemPhase> phases = new ArrayList<>();
        phases.addAll(List.of(UpdatePhase.values()));
        phases.addAll(List.of(RenderPhase.values()));
        this.phases = phases;
    }

    public FrameExecutionPlan buildPlan(Deque<Scene> sceneStack) {
        Map<SystemPhase, Long> result = new HashMap<>();
        for (SystemPhase phase : phases) {
            long activeTags = 0L;
            for (Scene scene: sceneStack) {
                SceneExecutionPolicy policy = scene.getExecutionPolicy();
                PhasePolicy phasePolicy = policy.phases().get(phase);
                if (phasePolicy == null) {
                    continue;
                }

                activeTags |= phasePolicy.tags();

                if (phasePolicy.propagation() == ExecutionPropagation.STOP) {
                    break;
                }
            }
            result.put(phase, activeTags);
        }
        return new FrameExecutionPlan(result);
    }
}
