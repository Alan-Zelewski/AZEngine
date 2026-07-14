package pl.engine.az.system;

import pl.engine.az.core.World;

public abstract class EcsSystem {
    protected final World world;

    protected EcsSystem(World world) {
        this.world = world;
    }
    
    public abstract SystemPhase phase();

    public abstract void update(double deltaTime);
}
