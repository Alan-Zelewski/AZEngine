package pl.engine.az.system;

import pl.engine.az.system.phase.RenderPhase;

import java.awt.*;

public interface RenderSystem extends EngineSystem {
    RenderPhase phase();
    void render(Graphics g, double alpha);
}
