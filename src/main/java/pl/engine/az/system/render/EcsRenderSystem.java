package pl.engine.az.system.render;

import pl.engine.az.ecs.World;
import pl.engine.az.system.EcsSystem;
import pl.engine.az.system.SystemPhase;

import java.awt.*;

public abstract class EcsRenderSystem extends EcsSystem {
    protected EcsRenderSystem(World world) {
        super(world);
    }

    @Override
    public final SystemPhase phase() {
        return SystemPhase.RENDER;
    }

    @Override
    public void update(double deltaTime) {
        throw new UnsupportedOperationException("Render systems cannot be updated. Use render().");
    }

    public abstract void render(Graphics g, double alpha);
}
