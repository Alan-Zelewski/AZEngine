package pl.engine.az.system.movement;

import org.dyn4j.geometry.Vector2;
import pl.engine.az.core.EngineContext;
import pl.engine.az.ecs.ComponentMapper;
import pl.engine.az.ecs.EntityCommandBuffer;
import pl.engine.az.ecs.Query;
import pl.engine.az.ecs.component.PhysicsBodyComponent;
import pl.engine.az.ecs.component.PositionComponent;
import pl.engine.az.system.UpdateSystem;
import pl.engine.az.system.phase.UpdatePhase;

public class PhysicsSystem implements UpdateSystem {
    private final EngineContext context;
    private final Query query;
    private final ComponentMapper<PositionComponent> positions;
    private final ComponentMapper<PhysicsBodyComponent> bodies;

    public PhysicsSystem(EngineContext context) {
        this.context = context;
        this.positions = context.getWorld().getMapper(PositionComponent.class);
        this.bodies = context.getWorld().getMapper(PhysicsBodyComponent.class);
        long mask = context.getWorld().maskOf(PositionComponent.class, PhysicsBodyComponent.class);
        this.query = context.getWorld().createQuery(mask);
    }

    @Override
    public UpdatePhase phase() {
        return UpdatePhase.PHYSICS_STEP;
    }

    @Override
    public void update(double deltaTime, EntityCommandBuffer commands) {
        context.getPhysics().step(deltaTime);
        for (int i = 0; i < query.size(); i++) {
            int entity = query.entityAt(i);
            PositionComponent position = positions.get(entity);
            PhysicsBodyComponent body = bodies.get(entity);

            Vector2 translation = body.body.getTransform().getTranslation();
            position.x = translation.x;
            position.y = translation.y;
        }

    }
}
