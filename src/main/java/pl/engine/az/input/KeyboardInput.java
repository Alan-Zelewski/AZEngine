package pl.engine.az.input;

import lombok.AllArgsConstructor;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@AllArgsConstructor
public class KeyboardInput extends KeyAdapter {
    private final InputManager inputManager;

    @Override
    public void keyPressed(KeyEvent e) {
        inputManager.postEvent(new RawInputEvent(RawInputEvent.Type.PRESSED, new InputKey(e.getKeyCode())));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        inputManager.postEvent(new RawInputEvent(RawInputEvent.Type.RELEASED, new InputKey(e.getKeyCode())));
    }
}
