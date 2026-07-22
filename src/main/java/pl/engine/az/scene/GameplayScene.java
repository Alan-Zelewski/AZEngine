package pl.engine.az.scene;

import pl.engine.az.core.EngineContext;
import pl.engine.az.core.SystemTag;
import pl.engine.az.system.phase.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class GameplayScene implements Scene{
    private final SceneExecutionPolicy executionPolicy;

    public GameplayScene() {
        Map<SystemPhase, PhasePolicy> policies = new HashMap<>();

        // Włączamy CORE dla fazy wejścia (PlayerInputSystem)
        long coreMask = SystemTag.toMask(EnumSet.of(SystemTag.CORE));
        policies.put(UpdatePhase.INPUT, new PhasePolicy(coreMask, ExecutionPropagation.STOP));

        // Włączamy GAMEPLAY dla fazy ruchu (MovementControllerSystem)
        long gameplayMask = SystemTag.toMask(EnumSet.of(SystemTag.GAMEPLAY));
        policies.put(UpdatePhase.MOVEMENT, new PhasePolicy(gameplayMask, ExecutionPropagation.STOP));

        // Włączamy GAMEPLAY dla fazy ogólnej (CollisionDamageSystem)
        policies.put(UpdatePhase.GAMEPLAY, new PhasePolicy(gameplayMask, ExecutionPropagation.STOP));

        // Włączamy CORE dla fazy fizyki (PhysicsSystem)
        policies.put(UpdatePhase.PHYSICS_STEP, new PhasePolicy(coreMask, ExecutionPropagation.STOP));

        // Włączamy RENDER dla renderowania świata (SquareRenderSystem)
        long renderMask = SystemTag.toMask(EnumSet.of(SystemTag.RENDER));
        policies.put(RenderPhase.WORLD, new PhasePolicy(renderMask, ExecutionPropagation.STOP));

        this.executionPolicy = new SceneExecutionPolicy(policies);
    }
    @Override
    public void onEnter(EngineContext context) {
        System.out.println("GameplayScene: onEnter");
    }

    @Override
    public void onExit(EngineContext context) {
        System.out.println("GameplayScene: onExit");
    }

    @Override
    public SceneExecutionPolicy getExecutionPolicy() {
        return executionPolicy;
    }
}
