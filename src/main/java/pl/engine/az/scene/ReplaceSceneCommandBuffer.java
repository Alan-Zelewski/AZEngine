package pl.engine.az.scene;

public record ReplaceSceneCommandBuffer(Scene scene) implements SceneCommandBuffer {
    @Override
    public void execute(SceneManager sceneManager) {
        sceneManager.applyReplace(scene);
    }
}
