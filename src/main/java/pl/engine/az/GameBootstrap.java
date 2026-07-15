package pl.engine.az;

import pl.engine.az.ecs.component.*;
import pl.engine.az.ecs.ComponentType;
import pl.engine.az.ecs.World;
import pl.engine.az.display.Display;
import pl.engine.az.input.*;
import pl.engine.az.system.input.PlayerInputSystem;
import pl.engine.az.system.movement.MovementControllerSystem;
import pl.engine.az.system.movement.MovementSystem;
import pl.engine.az.system.render.SquareRenderSystem;

import java.awt.*;

public class GameBootstrap {

    static void main() {
        World world = new World(10000);
        Display display = new Display(800, 600, "My ECS Game");
        InputManager inputManager = new InputManager();
        InputConfig config = InputConfigLoader.load("/input.json");
        inputManager.loadConfig(config);
        display.addInputListener(new KeyboardInput(inputManager));

        ComponentType<PositionComponent> positionType = world.registerComponent(PositionComponent.class);
        ComponentType<VelocityComponent> velocityType = world.registerComponent(VelocityComponent.class);
        ComponentType<RenderComponent> renderType = world.registerComponent(RenderComponent.class);
        ComponentType<DesiredMovementComponent> desiredType = world.registerComponent(DesiredMovementComponent.class);
        ComponentType<MovementStateComponent> stateType = world.registerComponent(MovementStateComponent.class);

        GameEngine engine = new GameEngine(display, inputManager);

        PlayerInputSystem playerInputSystem = new PlayerInputSystem(world, desiredType, inputManager);
        MovementControllerSystem movementControllerSystem = new MovementControllerSystem(world, desiredType, velocityType, stateType);
        MovementSystem movementSystem = new MovementSystem(world, positionType, velocityType);
        SquareRenderSystem squareRenderSystem = new SquareRenderSystem(world, positionType, renderType);

        engine.addSystem(playerInputSystem);
        engine.addSystem(movementControllerSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(squareRenderSystem);

        int player = world.createEntity();

        world.addComponent(
                player,
                positionType,
                new PositionComponent(100, 200)
        );

        world.addComponent(
                player,
                desiredType,
                new DesiredMovementComponent()
        );


        world.addComponent(
                player,
                stateType,
                new MovementStateComponent()
        );


        world.addComponent(
                player,
                velocityType,
                new VelocityComponent()
        );

        world.addComponent(
                player,
                renderType,
                new RenderComponent(50,30, Color.GREEN)
                );

        engine.start();
    }
}
