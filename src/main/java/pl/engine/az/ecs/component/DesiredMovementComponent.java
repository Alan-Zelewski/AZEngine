package pl.engine.az.ecs.component;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DesiredMovementComponent implements Component{
    public double x;
    public double y;
}
