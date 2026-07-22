package pl.engine.az.scene;

public sealed interface SceneCommandBuffer
        permits PushSceneCommandBuffer, PopSceneCommandBuffer, ReplaceSceneCommandBuffer {
    void execute(SceneManager sceneManager);
}
