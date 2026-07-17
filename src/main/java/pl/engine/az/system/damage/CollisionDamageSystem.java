package pl.engine.az.system.damage;

import pl.engine.az.core.EngineContext;
import pl.engine.az.ecs.EntityCommandBuffer;
import pl.engine.az.event.EventReader;
import pl.engine.az.event.types.CollisionEvent;
import pl.engine.az.system.phase.UpdatePhase;
import pl.engine.az.system.UpdateSystem;

import java.util.List;

public class CollisionDamageSystem implements UpdateSystem {
    private final EngineContext context;
    private final EventReader<CollisionEvent> collisionReader = new EventReader<>();

    public CollisionDamageSystem(EngineContext context) {
        this.context = context;
    }

    @Override
    public UpdatePhase phase() {
        return UpdatePhase.GAMEPLAY;
    }

    @Override
    public void update(double deltaTime, EntityCommandBuffer commands) {
        List<CollisionEvent> newCollisions = context.getCollisions().read(collisionReader);
        for (CollisionEvent event : newCollisions) {
            commands.destroyEntity(event.entityA());
        }
    }
}
