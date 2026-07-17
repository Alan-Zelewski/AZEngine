package pl.engine.az.core;

import pl.engine.az.ecs.DefaultEntityCommandBuffer;
import pl.engine.az.ecs.EntityCommandBuffer;
import pl.engine.az.ecs.World;
import pl.engine.az.system.phase.RenderPhase;
import pl.engine.az.system.phase.UpdatePhase;
import pl.engine.az.system.RenderSystem;
import pl.engine.az.system.UpdateSystem;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public final class SystemScheduler {
    private final EnumMap<UpdatePhase, List<UpdateSystem>> updateSystems = new EnumMap<>(UpdatePhase.class);
    private final EnumMap<RenderPhase, List<RenderSystem>> renderSystems = new EnumMap<>(RenderPhase.class);
    private final World world;

    public SystemScheduler(World world) {
        this.world = world;
        for (UpdatePhase phase : UpdatePhase.values()) {
            updateSystems.put(phase, new ArrayList<>());
        }
        for (RenderPhase phase : RenderPhase.values()) {
            renderSystems.put(phase, new ArrayList<>());
        }
    }

    public void register(UpdateSystem system) {
        updateSystems.get(system.phase()).add(system);
    }

    public void register(RenderSystem system) {
        renderSystems.get(system.phase()).add(system);
    }

    public void update(double dt) {
        for (UpdatePhase phase : UpdatePhase.values()) {
            EntityCommandBuffer phaseCommands = new DefaultEntityCommandBuffer();
            for (UpdateSystem system : updateSystems.get(phase)) {
                system.update(dt, phaseCommands);
            }
            phaseCommands.playback(world);
        }
    }

    public void render(Graphics g, double alpha) {
        for (RenderPhase phase : RenderPhase.values()) {
            for (RenderSystem system : renderSystems.get(phase)) {
                system.render(g, alpha);
            }
        }
    }
}
