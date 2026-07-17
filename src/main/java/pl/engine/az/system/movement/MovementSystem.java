package pl.engine.az.system.movement;

import pl.engine.az.ecs.EntityCommandBuffer;
import pl.engine.az.ecs.*;
import pl.engine.az.ecs.component.PositionComponent;
import pl.engine.az.ecs.component.VelocityComponent;
import pl.engine.az.system.phase.UpdatePhase;
import pl.engine.az.system.UpdateSystem;

public class MovementSystem implements UpdateSystem {

    private final Query query;
    private final ComponentMapper<PositionComponent> positions;
    private final ComponentMapper<VelocityComponent> velocities;

    public MovementSystem(
            World world,
            ComponentType<PositionComponent> positionType,
            ComponentType<VelocityComponent> velocityType
    ) {
        this.positions =
                new ComponentMapper<>(
                        positionType
                );
        this.velocities =
                new ComponentMapper<>(
                        velocityType
                );
        long mask = (1L << positionType.id()) | (1L << velocityType.id());
        this.query =
                world.createQuery(mask);
    }

    @Override
    public UpdatePhase phase() {
        return UpdatePhase.MOVEMENT;
    }

    @Override
    public void update(double deltaTime, EntityCommandBuffer commands) {
        for (int i = 0; i < query.size(); i++) {
            int entity =
                    query.entityAt(i);
            PositionComponent position =
                    positions.get(entity);
            VelocityComponent velocity =
                    velocities.get(entity);
            position.x += velocity.x * deltaTime;
            position.y += velocity.y * deltaTime;
        }
    }
}
