package pl.engine.az;

import pl.engine.az.core.EngineContext;
import pl.engine.az.core.ExecutionPlanner;
import pl.engine.az.core.FrameExecutionPlan;
import pl.engine.az.display.Display;
import pl.engine.az.core.SystemScheduler;
import pl.engine.az.input.InputManager;
import pl.engine.az.scene.SceneManager;

import java.awt.*;
import java.util.concurrent.locks.LockSupport;

public class GameEngine implements Runnable {
    private volatile boolean running = false;
    private static final double TIME_STEP = 1.0 / 60.0;
    private static final long TARGET_FRAME_NANOS = 1_000_000_000 / 60;
    private final Display display;
    private final InputManager inputManager;
    private final SystemScheduler scheduler;
    private final SceneManager sceneManager;
    private final ExecutionPlanner planner;
    private final EngineContext context;
    // Zbuforowany plan klatki
    private FrameExecutionPlan currentPlan;

    public GameEngine(Display display, InputManager inputManager, SystemScheduler scheduler,
            SceneManager sceneManager,
            ExecutionPlanner planner,
            EngineContext context
    ) {
        this.display = display;
        this.inputManager = inputManager;
        this.scheduler = scheduler;
        this.sceneManager = sceneManager;
        this.planner = planner;
        this.context = context;
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        new Thread(this, "Game Thread").start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double accumulator = 0.0;
        currentPlan = planner.buildPlan(sceneManager.getStack());

        while (running) {
            long frameStart = System.nanoTime();
            double deltaTime = (frameStart - lastTime) / 1_000_000_000.0;
            lastTime = frameStart;
            deltaTime = Math.min(deltaTime, 0.25);
            accumulator += deltaTime;

            while (accumulator >= TIME_STEP) {
                update(TIME_STEP);
                accumulator -= TIME_STEP;
            }

            double alpha = accumulator / TIME_STEP;
            render(alpha);

            limitFrameRate(frameStart);
        }
    }

    private void update(double deltaTime) {
        sceneManager.flush();
        if (sceneManager.isDirty()) {
            currentPlan = planner.buildPlan(sceneManager.getStack());
            sceneManager.clearDirty();
        }
        inputManager.beginFrame();
        scheduler.update(deltaTime, currentPlan, context);
    }

    private void render(double alpha) {
        Graphics g = display.beginFrame();
        try {
            scheduler.render(g, alpha, currentPlan);
        } finally {
            g.dispose();
        }

        display.endFrame();
    }

    private void limitFrameRate(long frameStartNanos) {
        long sleepNanos = TARGET_FRAME_NANOS - (System.nanoTime() - frameStartNanos);
        if (sleepNanos > 0) {
            LockSupport.parkNanos(sleepNanos);
        }
    }
}