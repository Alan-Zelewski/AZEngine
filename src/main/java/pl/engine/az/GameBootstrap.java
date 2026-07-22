package pl.engine.az;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import pl.engine.az.core.EngineContext;
import pl.engine.az.core.ExecutionPlanner;
import pl.engine.az.core.PhysicsWorld;
import pl.engine.az.core.SystemScheduler;
import pl.engine.az.display.Display;
import pl.engine.az.ecs.ComponentScanner;
import pl.engine.az.ecs.World;
import pl.engine.az.ecs.component.*;
import pl.engine.az.ecs.component.Component;
import pl.engine.az.input.InputConfig;
import pl.engine.az.input.InputConfigLoader;
import pl.engine.az.input.InputManager;
import pl.engine.az.input.KeyboardInput;
import pl.engine.az.scene.GameplayScene;
import pl.engine.az.scene.SceneManager;
import pl.engine.az.system.input.PlayerInputSystem;
import pl.engine.az.system.movement.MovementControllerSystem;
import pl.engine.az.system.movement.PhysicsSystem;
import pl.engine.az.system.render.SquareRenderSystem;

import java.awt.*;
import java.util.List;

public class GameBootstrap {

    static void main() {
        World world = new World(10000);
        Display display = new Display(800, 600, "My ECS Game");
        InputManager inputManager = new InputManager();
        InputConfig config = InputConfigLoader.load("/input.json");
        inputManager.loadConfig(config);
        display.addInputListener(new KeyboardInput(inputManager));

        PhysicsWorld physicsWorld = new PhysicsWorld();
        EngineContext context = new EngineContext(world, physicsWorld);
        SceneManager sceneManager = new SceneManager(context);
        ExecutionPlanner planner = new ExecutionPlanner();
        SystemScheduler scheduler = new SystemScheduler();

        List<Class<? extends Component>> components =
                ComponentScanner.findComponents("pl.engine.az.ecs.component");
        for (Class<? extends Component> c : components) {
            world.registerComponent(c);
        }

        scheduler.register(new PlayerInputSystem(world, inputManager));
        scheduler.register(new MovementControllerSystem(world));
        scheduler.register(new PhysicsSystem(context));
        scheduler.register(new SquareRenderSystem(world));

        // Tworzenie granic ekranu (Statyczne - INFINITE mass)
        createWall(physicsWorld, 400, -10, 800, 20); // Góra
        createWall(physicsWorld, 400, 610, 800, 20); // Dół
        createWall(physicsWorld, -10, 300, 20, 600); // Lewo
        createWall(physicsWorld, 810, 300, 20, 600); // Prawo

        // Tworzenie statycznego bloku na środku ekranu
        createStaticObstacle(world, context, 400, 300, 100, 100);

        // Tworzenie dynamicznego gracza
        createPlayer(world, context, 100, 100);

        sceneManager.requestPush(new GameplayScene());

        GameEngine engine = new GameEngine(display, inputManager, scheduler, sceneManager, planner, context);

        engine.start();
    }

    private static void createWall(PhysicsWorld physicsWorld, double x, double y, double w, double h) {
        Body wall = new Body();
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(w, h));
        fixture.setFriction(0.0); // Brak tarcia
        wall.addFixture(fixture);
        wall.translate(x, y);
        wall.setMass(MassType.INFINITE);
        physicsWorld.getDyn4jWorld().addBody(wall);
    }

    private static void createStaticObstacle(
            World world,
            EngineContext context,
            double x, double y, double w, double h
    ) {
        int entity = world.createEntity();
        Body body = new Body();
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(w, h));
        fixture.setFriction(0.0); // Brak tarcia
        body.addFixture(fixture);
        body.translate(x, y);
        body.setMass(MassType.INFINITE);
        context.getPhysics().getDyn4jWorld().addBody(body);

        world.addComponent(entity, world.getComponentType(PositionComponent.class), new PositionComponent(x, y));
        world.addComponent(entity, world.getComponentType(PhysicsBodyComponent.class), new PhysicsBodyComponent(body));
        world.addComponent(
                entity,
                world.getComponentType(RenderComponent.class),
                new RenderComponent((int) w, (int) h, Color.RED));
    }

    private static void createPlayer(
            World world,
            EngineContext ctx,
            double x,
            double y
    ) {
        int player = world.createEntity();
        Body body = new Body();
        BodyFixture fixture = body.addFixture(Geometry.createRectangle(50, 30));
        fixture.setFriction(0.0);
        fixture.setRestitution(0.0);
        body.translate(x, y);
        body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        body.setAtRestDetectionEnabled(false);
        body.setAngularVelocity(0.0);

        ctx.getPhysics().getDyn4jWorld().addBody(body);

        world.addComponent(player, world.getComponentType(PositionComponent.class), new PositionComponent(x, y));
        world.addComponent(
                player,
                world.getComponentType(DesiredMovementComponent.class),
                new DesiredMovementComponent());

        MovementStateComponent state = new MovementStateComponent();
        state.speed = 200.0;
        world.addComponent(player, world.getComponentType(MovementStateComponent.class), state);
        world.addComponent(player, world.getComponentType(PhysicsBodyComponent.class), new PhysicsBodyComponent(body));
        world.addComponent(
                player,
                world.getComponentType(RenderComponent.class),
                new RenderComponent(50, 30, Color.GREEN));
    }

}
