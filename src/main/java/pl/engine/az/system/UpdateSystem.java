package pl.engine.az.system;

import pl.engine.az.ecs.EntityCommandBuffer;
import pl.engine.az.system.phase.UpdatePhase;

public interface UpdateSystem extends EngineSystem {
    void update(double deltaTime, EntityCommandBuffer commands);
}
