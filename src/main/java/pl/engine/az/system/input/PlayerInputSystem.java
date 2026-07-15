package pl.engine.az.system.input;

import pl.engine.az.ecs.ComponentMapper;
import pl.engine.az.ecs.ComponentType;
import pl.engine.az.ecs.Query;
import pl.engine.az.ecs.World;
import pl.engine.az.ecs.component.DesiredMovementComponent;
import pl.engine.az.input.Action;
import pl.engine.az.input.InputManager;
import pl.engine.az.system.EcsSystem;
import pl.engine.az.system.SystemPhase;

public class PlayerInputSystem extends EcsSystem {
    private final InputManager inputManager;
    private final Query query;
    private final ComponentMapper<DesiredMovementComponent> movements;

    public PlayerInputSystem(
            World world,
            ComponentType<DesiredMovementComponent> movementType,
            InputManager inputManager
    ) {
        super(world);
        this.inputManager = inputManager;
        this.movements = new ComponentMapper<>(movementType);
        long mask = 1L << movementType.id();
        this.query = world.createQuery(mask);
    }

    @Override
    public SystemPhase phase() {
        return SystemPhase.INPUT;
    }

    @Override
    public void update(double deltaTime) {
        for (int i = 0; i < query.size(); i++) {
            int entity = query.entityAt(i);
            DesiredMovementComponent desired = movements.get(entity);
            desired.x = 0;
            desired.y = 0;

            if (inputManager.isActionActive(Action.MOVE_LEFT)) {
                desired.x -= 1;
            }
            if (inputManager.isActionActive(Action.MOVE_RIGHT)) {
                desired.x += 1;
            }
            if (inputManager.isActionActive(Action.MOVE_UP)) {
                desired.y -= 1;
            }
            if (inputManager.isActionActive(Action.MOVE_DOWN)) {
                desired.y += 1;
            }
            normalize(desired);
        }
    }

    private void normalize(DesiredMovementComponent movement) {
        double length = Math.sqrt(
                movement.x * movement.x +
                        movement.y * movement.y
        );
        if (length > 0) {
            movement.x /= length;
            movement.y /= length;
        }
    }
}
