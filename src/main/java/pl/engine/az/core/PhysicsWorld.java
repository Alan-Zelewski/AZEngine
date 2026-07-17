package pl.engine.az.core;

import org.dyn4j.dynamics.Body;
import org.dyn4j.world.World;

public class PhysicsWorld {
    private final World<Body> dyn4jWorld = new World<>();

    public World<Body> getDyn4jWorld() {
        return dyn4jWorld;
    }

    public void step(double dt) {
        dyn4jWorld.update(dt);
    }
}
