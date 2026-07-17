package pl.engine.az.system.lifetime;

import pl.engine.az.ecs.EntityCommandBuffer;
import pl.engine.az.ecs.*;
import pl.engine.az.ecs.component.LifetimeComponent;
import pl.engine.az.system.phase.UpdatePhase;
import pl.engine.az.system.UpdateSystem;

public class LifetimeSystem implements UpdateSystem {
    private final Query query;
    private final ComponentMapper<LifetimeComponent> lifetimes;

    public LifetimeSystem(World world, ComponentType<LifetimeComponent> lifetimeType) {
        this.lifetimes = new ComponentMapper<>(lifetimeType);
        this.query = world.createQuery(1L << lifetimeType.id());
    }

    @Override
    public UpdatePhase phase() {
        return UpdatePhase.GAMEPLAY;
    }

    @Override
    public void update(double deltaTime, EntityCommandBuffer commands) {
        for (int i = 0; i < query.size(); i++) {
            int entity = query.entityAt(i);
            LifetimeComponent life = lifetimes.get(entity);
            life.timeLeft -= deltaTime;
            if (life.timeLeft <= 0) {
                commands.destroyEntity(entity);
            }
        }
    }
}
