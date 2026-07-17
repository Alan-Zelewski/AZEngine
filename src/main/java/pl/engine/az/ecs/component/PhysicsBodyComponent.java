package pl.engine.az.ecs.component;
import lombok.AllArgsConstructor;
import org.dyn4j.dynamics.Body;

@AllArgsConstructor
public class PhysicsBodyComponent implements Component{
    public final Body body;
}
