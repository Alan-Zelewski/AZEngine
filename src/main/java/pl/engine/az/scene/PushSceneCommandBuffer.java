package pl.engine.az.scene;

public record PushSceneCommandBuffer(Scene scene) implements SceneCommandBuffer {

    @Override
    public void execute(SceneManager sceneManager) {
        sceneManager.applyPush(scene);
    }
}
