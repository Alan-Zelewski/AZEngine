package pl.engine.az;

import pl.engine.az.components.PositionComponent;
import pl.engine.az.components.RenderComponent;
import pl.engine.az.components.VelocityComponent;
import pl.engine.az.core.ComponentType;
import pl.engine.az.core.World;
import pl.engine.az.display.Display;
import pl.engine.az.system.MovementSystem;
import pl.engine.az.system.render.SquareRenderSystem;

import java.awt.*;

public class GameBootstrap {

    static void main() {
        World world = new World(10000);
        Display display = new Display(800, 600, "My ECS Game");

        ComponentType<PositionComponent> positionType = world.registerComponent(PositionComponent.class);
        ComponentType<VelocityComponent> velocityType = world.registerComponent(VelocityComponent.class);
        ComponentType<RenderComponent> renderType = world.registerComponent(RenderComponent.class);

        GameEngine engine = new GameEngine(world, display);

        MovementSystem movementSystem = new MovementSystem(world, positionType, velocityType);
        SquareRenderSystem squareRenderSystem = new SquareRenderSystem(world, positionType, renderType);

        engine.addSystem(movementSystem);
        engine.addSystem(squareRenderSystem);

        int player =
                world.createEntity();


        world.addComponent(
                player,
                positionType,
                new PositionComponent(100, 200)
        );


        world.addComponent(
                player,
                velocityType,
                new VelocityComponent(50, 22)
        );

        world.addComponent(
                player,
                renderType,
                new RenderComponent(10,5, Color.GREEN)
                );

        engine.start();
    }
}
