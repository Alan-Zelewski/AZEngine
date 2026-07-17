package pl.engine.az.system.movement;

import pl.engine.az.ecs.ComponentMapper;
import pl.engine.az.ecs.EntityCommandBuffer;
import pl.engine.az.ecs.Query;
import pl.engine.az.ecs.World;
import pl.engine.az.ecs.component.DesiredMovementComponent;
import pl.engine.az.ecs.component.MovementStateComponent;
import pl.engine.az.ecs.component.PhysicsBodyComponent;
import pl.engine.az.system.UpdateSystem;
import pl.engine.az.system.phase.UpdatePhase;

public class MovementControllerSystem implements UpdateSystem {

    private final Query query;
    private final ComponentMapper<DesiredMovementComponent> desiredMapper;
    private final ComponentMapper<PhysicsBodyComponent> bodies;
    private final ComponentMapper<MovementStateComponent> states;

    public MovementControllerSystem(World world) {
        desiredMapper = world.getMapper(DesiredMovementComponent.class);
        bodies = world.getMapper(PhysicsBodyComponent.class);
        states = world.getMapper(MovementStateComponent.class);
        long mask = world.maskOf(
                DesiredMovementComponent.class,
                PhysicsBodyComponent.class,
                MovementStateComponent.class);
        query = world.createQuery(mask);
    }

    @Override
    public UpdatePhase phase() {
        return UpdatePhase.MOVEMENT;
    }

    @Override
    public void update(double deltaTime, EntityCommandBuffer commands) {
        for (int i = 0; i < query.size(); i++) {
            int entity = query.entityAt(i);
            DesiredMovementComponent desired = desiredMapper.get(entity);
            MovementStateComponent state = states.get(entity);
            PhysicsBodyComponent phys = bodies.get(entity);
            double targetX = desired.x * state.speed;
            double targetY = desired.y * state.speed;
            phys.body.setLinearVelocity(targetX, targetY);
//            if (!state.movementEnabled) {
//                velocity.x = approach(velocity.x, 0, state.deceleration * deltaTime);
//                velocity.y = approach(velocity.y, 0, state.deceleration * deltaTime);
//                continue;
//            }
//            double targetX = desired.x * state.speed;
//            boolean changingDirectionX = Math.signum(velocity.x) != Math.signum(targetX);
//            double targetY = desired.y * state.speed;
//            boolean changingDirectionY = Math.signum(velocity.y) != Math.signum(targetY);
//            double changeRateX = changingDirectionX
//                    ? state.deceleration
//                    : desired.x != 0 ? state.acceleration : state.deceleration;
//            double changeRateY = changingDirectionY
//                    ? state.deceleration
//                    : desired.y != 0 ? state.acceleration : state.deceleration;
//            double maxChangeX = changeRateX * deltaTime;
//            double maxChangeY = changeRateY * deltaTime;
//            velocity.x = approach(velocity.x, targetX, maxChangeX);
//            velocity.y = approach(velocity.y, targetY, maxChangeY);

        }
    }

    private double approach(double current, double target, double maxDelta) {
        if (current < target) {
            return Math.min(current + maxDelta, target);
        }
        if (current > target) {
            return Math.max(current - maxDelta, target);
        }
        return target;
    }
}
