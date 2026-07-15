package pl.engine.az.system.movement;

import pl.engine.az.ecs.component.PositionComponent;
import pl.engine.az.ecs.component.VelocityComponent;
import pl.engine.az.ecs.ComponentMapper;
import pl.engine.az.ecs.ComponentType;
import pl.engine.az.ecs.Query;
import pl.engine.az.ecs.World;
import pl.engine.az.system.EcsSystem;
import pl.engine.az.system.SystemPhase;

public class MovementSystem extends EcsSystem {

    private final Query query;
    private final ComponentMapper<PositionComponent> positions;
    private final ComponentMapper<VelocityComponent> velocities;

    public MovementSystem(
            World world,
            ComponentType<PositionComponent> positionType,
            ComponentType<VelocityComponent> velocityType
    ) {
        super(world);
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
    public SystemPhase phase() {
        return SystemPhase.UPDATE;
    }

    @Override
    public void update(double deltaTime) {
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
