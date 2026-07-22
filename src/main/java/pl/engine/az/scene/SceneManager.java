package pl.engine.az.scene;

import lombok.Getter;
import pl.engine.az.core.EngineContext;

import java.util.*;

public class SceneManager {
    @Getter
    private final Deque<Scene> stack = new ArrayDeque<>();
    private final Queue<SceneCommandBuffer> pendingCommands = new ArrayDeque<>();
    private final EngineContext context;
    @Getter
    private boolean dirty = true;
    public SceneManager(EngineContext context) {
        this.context = context;
    }

    public void requestPush(Scene scene) {
        pendingCommands.add(new PushSceneCommandBuffer(scene));
    }

    public void requestPop() {
        pendingCommands.add(new PopSceneCommandBuffer());
    }

    public void requestReplace(Scene scene) {
        pendingCommands.add(new ReplaceSceneCommandBuffer(scene));
    }

    public Scene peek() {
        return stack.peek();
    }

    public void clearDirty() {
        this.dirty = false;
    }

    public void flush() {
        if (pendingCommands.isEmpty()) {
            return;
        }
        while (!pendingCommands.isEmpty()) {
            SceneCommandBuffer command = pendingCommands.poll();
            command.execute(this);
        }
        dirty = true;
    }

    void applyPush(Scene scene) {
        Objects.requireNonNull(scene);
        Scene current = stack.peek();
        if (current != null) {
            current.onDeactivate(context);
        }
        stack.push(scene);
        scene.onEnter(context);
        scene.onActivate(context);
    }

    void applyPop() {
        if (stack.isEmpty()) {
            return;
        }
        Scene removed = stack.peek();
        removed.onDeactivate(context);
        stack.pop();
        removed.onExit(context);
        Scene current = stack.peek();
        if (current != null) {
            current.onActivate(context);
        }
    }

    void applyReplace(Scene scene) {
        Objects.requireNonNull(scene);
        if (stack.isEmpty()) {
            applyPush(scene);
            return;
        }
        Scene removed = stack.peek();
        removed.onDeactivate(context);
        stack.pop();
        removed.onExit(context);
        stack.push(scene);
        scene.onEnter(context);
        scene.onActivate(context);
    }
}
