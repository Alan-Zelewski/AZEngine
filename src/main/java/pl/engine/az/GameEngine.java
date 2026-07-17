package pl.engine.az;

import pl.engine.az.display.Display;
import pl.engine.az.core.SystemScheduler;
import pl.engine.az.input.InputManager;

import java.awt.*;
import java.util.concurrent.locks.LockSupport;

public class GameEngine implements Runnable {
    private volatile boolean running = false;
    private static final double TIME_STEP = 1.0 / 60.0;
    private static final long TARGET_FRAME_NANOS = 1_000_000_000 / 60;
    private final Display display;
    private final InputManager inputManager;
    private final SystemScheduler scheduler;

    public GameEngine(Display display, InputManager inputManager, SystemScheduler scheduler) {
        this.display = display;
        this.inputManager = inputManager;
        this.scheduler = scheduler;
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
        inputManager.beginFrame();
        scheduler.update(deltaTime);
    }

    private void render(double alpha) {
        Graphics g = display.beginFrame();
        try {
            scheduler.render(g, alpha);
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