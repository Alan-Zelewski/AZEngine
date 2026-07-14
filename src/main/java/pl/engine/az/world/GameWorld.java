package pl.engine.az.world;

import lombok.Getter;

import java.awt.*;

@Getter
public class GameWorld {
    private Rectangle bounds;

    public GameWorld(int width, int height) {
        this.bounds = new Rectangle(0,0, width, height);
    }
}
