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
    private final EnumMap<UpdatePhase, List<UpdateSystemNode>> updateSystems = new EnumMap<>(UpdatePhase.class);
    private final EnumMap<RenderPhase, List<RenderSystemNode>> renderSystems = new EnumMap<>(RenderPhase.class);

    public SystemScheduler() {
        for (UpdatePhase phase : UpdatePhase.values()) {
            updateSystems.put(phase, new ArrayList<>());
        }
        for (RenderPhase phase : RenderPhase.values()) {
            renderSystems.put(phase, new ArrayList<>());
        }
    }

    public void register(UpdateSystem system) {
        UpdateSystemNode node = new UpdateSystemNode(system);
        UpdatePhase phase = (UpdatePhase) node.getDescriptor().phase();
        updateSystems.get(phase).add(node);
    }

    public void register(RenderSystem system) {
        RenderSystemNode node = new RenderSystemNode(system);
        RenderPhase phase = (RenderPhase) node.getDescriptor().phase();
        renderSystems.get(phase).add(node);
    }

    public void update(double dt, FrameExecutionPlan plan, EngineContext context) {
        for (UpdatePhase phase : UpdatePhase.values()) {
            long activeMask = plan.getMask(phase);

            if (activeMask == 0L) continue;

            EntityCommandBuffer phaseCommands = new DefaultEntityCommandBuffer();

            for (UpdateSystemNode node : updateSystems.get(phase)) {
                if (node.canRun(activeMask)) {
                    node.getSystem().update(dt, phaseCommands);
                }
            }
            phaseCommands.playback(context.getWorld());
        }
    }

    public void render(Graphics g, double alpha, FrameExecutionPlan plan) {
        for (RenderPhase phase : RenderPhase.values()) {
            long activeMask = plan.getMask(phase);

            if (activeMask == 0L) continue;

            for(RenderSystemNode node : renderSystems.get(phase)) {
                if (node.canRun(activeMask)) {
                    node.getSystem().render(g, alpha);
                }
            }
        }
    }
}
