package pl.engine.az.scene;

public record PopSceneCommandBuffer() implements SceneCommandBuffer {
    @Override
    public void execute(SceneManager sceneManager) {
        sceneManager.applyPop();
    }
}
