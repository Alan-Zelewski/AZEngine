package pl.engine.az.core;

import pl.engine.az.scene.Scene;
import pl.engine.az.scene.SceneExecutionPolicy;
import pl.engine.az.system.phase.RenderPhase;
import pl.engine.az.system.phase.SystemPhase;
import pl.engine.az.system.phase.UpdatePhase;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ExecutionPlanner {

    public FrameExecutionPlan buildPlan(Deque<Scene> sceneStack) {
        Map<SystemPhase, Long> map = new HashMap<>();

        for (UpdatePhase phase : UpdatePhase.values()) {
            long mask = 0L;
            for (Scene scene : sceneStack) {
                SceneExecutionPolicy pol = scene.getExecutionPolicy();
                mask |= pol.updateMask;
                if (pol.stopUpdate)
                    break;
            }
            map.put(phase, mask);
        }

        for (RenderPhase phase : RenderPhase.values()) {
            long mask = 0L;
            for (Scene scene : sceneStack) {
                SceneExecutionPolicy pol = scene.getExecutionPolicy();
                mask |= pol.renderMask;
                if (pol.stopRender)
                    break;
            }
            map.put(phase, mask);
        }
        return new FrameExecutionPlan(map);
    }
}
