package pl.engine.az.system;

import pl.engine.az.components.PositionComponent;
import pl.engine.az.components.VelocityComponent;
import pl.engine.az.core.ComponentMapper;
import pl.engine.az.core.ComponentType;
import pl.engine.az.core.Query;
import pl.engine.az.core.World;

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
