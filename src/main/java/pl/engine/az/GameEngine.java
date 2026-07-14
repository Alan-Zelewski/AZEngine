package pl.engine.az;

import pl.engine.az.core.World;
import pl.engine.az.display.Display;
import pl.engine.az.system.EcsSystem;
import pl.engine.az.system.SystemPhase;
import pl.engine.az.system.render.EcsRenderSystem;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class GameEngine implements Runnable {
    private volatile boolean running = false;
    private static final double TIME_STEP = 1.0 / 60.0;
    private final World world;
    private final Display display;
    private final EnumMap<SystemPhase, List<EcsSystem>> systems = new EnumMap<>(SystemPhase.class);

    public GameEngine(World world, Display display) {
        this.world = world;
        this.display = display;
        for (SystemPhase phase : SystemPhase.values()) {
            systems.put(
                    phase,
                    new ArrayList<>()
            );
        }
    }

    public void addSystem(EcsSystem system) {
        systems.get(system.phase())
                .add(system);
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

            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
            lastTime = currentTime;

            deltaTime = Math.min(deltaTime, 0.25);

            accumulator += deltaTime;

            while (accumulator >= TIME_STEP) {
                update(TIME_STEP);
                accumulator -= TIME_STEP;
            }

            double alpha = accumulator / TIME_STEP;
            render(alpha);
        }
    }

    private void update(double deltaTime) {
        for (SystemPhase phase : SystemPhase.values()) {
            if (phase == SystemPhase.RENDER || phase == SystemPhase.RENDER_PREPARE) {
                continue;
            }
            for (EcsSystem system : systems.get(phase)) {
                system.update(deltaTime);
            }
        }
    }

    private void render(double alpha) {
        Graphics g = display.beginFrame();
        try {
            for (SystemPhase phase : List.of(SystemPhase.RENDER_PREPARE, SystemPhase.RENDER)) {
                for (EcsSystem system : systems.get(phase)) {
                    if (system instanceof EcsRenderSystem renderSystem) {
                        renderSystem.render(g, alpha);
                    }
                }
            }
        } finally {
            g.dispose();
        }

        display.endFrame();
    }

}

