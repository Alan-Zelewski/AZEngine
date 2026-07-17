package pl.engine.az.ecs.component;

import lombok.AllArgsConstructor;

import java.awt.*;

@AllArgsConstructor
public class RenderComponent implements Component{
    public int width;
    public int height;
    public Color color;
}
