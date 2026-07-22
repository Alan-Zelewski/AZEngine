package pl.engine.az.system.render;

import pl.engine.az.core.SystemDescriptor;
import pl.engine.az.core.SystemTag;
import pl.engine.az.ecs.ComponentMapper;
import pl.engine.az.ecs.Query;
import pl.engine.az.ecs.World;
import pl.engine.az.ecs.component.PositionComponent;
import pl.engine.az.ecs.component.RenderComponent;
import pl.engine.az.system.RenderSystem;
import pl.engine.az.system.phase.RenderPhase;

import java.awt.*;
import java.util.EnumSet;

public class SquareRenderSystem implements RenderSystem {
    public static final SystemDescriptor DESCRIPTOR = new SystemDescriptor(
            "SquareRenderer",
            RenderPhase.WORLD,
            EnumSet.of(SystemTag.RENDER)
    );
    private final Query query;
    private final ComponentMapper<PositionComponent> positions;
    private final ComponentMapper<RenderComponent> renders;

    public SquareRenderSystem(World world) {
        positions = world.getMapper(PositionComponent.class);
        renders = world.getMapper(RenderComponent.class);
        long mask = world.maskOf(PositionComponent.class, RenderComponent.class);
        query = world.createQuery(mask);
    }

    @Override
    public SystemDescriptor descriptor() {
        return DESCRIPTOR;
    }

    @Override
    public void render(Graphics g, double alpha) {
        for (int i = 0; i < query.size(); i++) {
            int entity = query.entityAt(i);
            PositionComponent position = positions.get(entity);
            RenderComponent render = renders.get(entity);
            g.setColor(render.color);

            // 1. Obliczamy pozycję lewego górnego rogu na podstawie środka z fizyki
            int drawX = (int) (position.x - (render.width / 2.0));
            int drawY = (int) (position.y - (render.height / 2.0));
            g.fillRect(drawX, drawY, render.width, render.height);
        }
    }
}
