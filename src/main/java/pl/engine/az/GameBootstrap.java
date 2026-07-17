package pl.engine.az;

import pl.engine.az.core.EngineContext;
import pl.engine.az.core.PhysicsWorld;
import pl.engine.az.core.SystemScheduler;
import pl.engine.az.ecs.*;
import pl.engine.az.ecs.component.*;
import pl.engine.az.display.Display;
import pl.engine.az.input.*;
import pl.engine.az.system.input.PlayerInputSystem;
import pl.engine.az.system.lifetime.LifetimeSystem;
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

        PhysicsWorld physicsWorld = dt -> {};
        EngineContext context = new EngineContext(world, physicsWorld);
        SystemScheduler scheduler = new SystemScheduler(world);

        ComponentType<PositionComponent> positionType = world.registerComponent(PositionComponent.class);
        ComponentType<VelocityComponent> velocityType = world.registerComponent(VelocityComponent.class);
        ComponentType<RenderComponent> renderType = world.registerComponent(RenderComponent.class);
        ComponentType<DesiredMovementComponent> desiredType = world.registerComponent(DesiredMovementComponent.class);
        ComponentType<MovementStateComponent> stateType = world.registerComponent(MovementStateComponent.class);
        ComponentType<LifetimeComponent> lifetimeType = world.registerComponent(LifetimeComponent.class);


        scheduler.register(new PlayerInputSystem(world, desiredType, inputManager));
        scheduler.register(new MovementControllerSystem(world, desiredType, velocityType, stateType));
        scheduler.register(new MovementSystem(world, positionType, velocityType));
        scheduler.register(new SquareRenderSystem(world, positionType, renderType));
        scheduler.register(new LifetimeSystem(world, lifetimeType));

        GameEngine engine = new GameEngine(display, inputManager, scheduler);

        int player = world.createEntity();
        world.addComponent(player, positionType, new PositionComponent(100, 200));
        world.addComponent(player, desiredType, new DesiredMovementComponent());
        world.addComponent(player, stateType, new MovementStateComponent());
        world.addComponent(player, velocityType, new VelocityComponent());
        world.addComponent(player, renderType, new RenderComponent(50,30, Color.GREEN));

        int particle = world.createEntity();
        world.addComponent(particle, positionType, new PositionComponent(50, 50));
        world.addComponent(particle, lifetimeType, new LifetimeComponent(3.0));
        world.addComponent(particle, velocityType, new VelocityComponent(100,100));
        world.addComponent(particle, renderType, new RenderComponent(50,30, Color.RED));

        engine.start();
    }
}
