package pl.engine.az.system.render;

import pl.engine.az.components.PositionComponent;
import pl.engine.az.components.RenderComponent;
import pl.engine.az.core.ComponentMapper;
import pl.engine.az.core.ComponentType;
import pl.engine.az.core.Query;
import pl.engine.az.core.World;

import java.awt.*;

public class SquareRenderSystem extends EcsRenderSystem {
    private final Query query;
    private final ComponentMapper<PositionComponent> positions;
    private final ComponentMapper<RenderComponent> renders;

    public SquareRenderSystem(
            World world,
            ComponentType<PositionComponent> positionType,
            ComponentType<RenderComponent> renderType
    ) {

        super(world);
        positions = new ComponentMapper<>(positionType);
        renders = new ComponentMapper<>(renderType);
        long mask = (1L << positionType.id()) | (1L << renderType.id());
        query = world.createQuery(mask);
    }


    @Override
    public void render(Graphics g, double alpha) {
        for(int i =0; i < query.size(); i++) {
            int entity = query.entityAt(i);
            PositionComponent position = positions.get(entity);
            RenderComponent render = renders.get(entity);
            g.setColor(render.color);

            g.fillRect((int) position.x, (int) position.y, render.width, render.height);
        }
    }
}
