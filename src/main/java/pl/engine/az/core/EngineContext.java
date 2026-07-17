package pl.engine.az.core;

import lombok.Getter;
import lombok.Setter;
import pl.engine.az.ecs.World;
import pl.engine.az.event.EventChannel;
import pl.engine.az.event.OverflowPolicy;
import pl.engine.az.event.types.CollisionEvent;
import pl.engine.az.event.types.PlayerDeathEvent;

import java.awt.event.InputEvent;

@Getter
@Setter
public final class EngineContext {
    private final World world;
    private final PhysicsWorld physics;

    private final EventChannel<CollisionEvent> collisions;
    private final EventChannel<PlayerDeathEvent> deaths;
    private final EventChannel<InputEvent> inputs;

    public EngineContext(World world, PhysicsWorld physics) {
        this.world = world;
        this.physics = physics;
        this.collisions = new EventChannel<>(256, OverflowPolicy.DROP_OLDEST);
        this.deaths = new EventChannel<>(32, OverflowPolicy.GROW);
        this.inputs = new EventChannel<>(128, OverflowPolicy.THROW);
    }
}
