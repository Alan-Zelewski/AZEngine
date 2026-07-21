package pl.engine.az.scene;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SceneExecutionPolicy {
    public final long updateMask;
    public final boolean stopUpdate;
    public final long renderMask;
    public final boolean stopRender;
}
