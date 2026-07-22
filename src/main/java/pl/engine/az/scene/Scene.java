package pl.engine.az.scene;

import pl.engine.az.core.EngineContext;

public interface Scene {
    void onEnter(EngineContext context);
    void onExit(EngineContext context);

    default void onActivate(EngineContext context) {}
    default void onDeactivate(EngineContext context) {}

    SceneExecutionPolicy getExecutionPolicy();
}
